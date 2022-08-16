package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.control.Controller;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.MostCrowdedStrategyBuilder;
import simulator.factories.MoveAllStrategyBuilder;
import simulator.factories.MoveFirstStrategyBuilder;
import simulator.factories.NewCityRoadEventBuilder;
import simulator.factories.NewInterCityRoadEventBuilder;
import simulator.factories.NewJunctionEventBuilder;
import simulator.factories.NewVehicleEventBuilder;
import simulator.factories.RoundRobinStrategyBuilder;
import simulator.factories.SetContClassEventBuilder;
import simulator.factories.SetWeatherEventBuilder;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;

public class Main {

	private final static int _timeLimitDefaultValue = 10;
	private static int _ticks = -1;
	private static String _inFile = null;
	private static String _outFile = null;
	private static String _mode = null;
	private static Factory<Event> _eventsFactory = null;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseModeOption(line);
			parseInFileOption(line);
			if(_mode.equals("console")) {
			parseOutFileOption(line);
			parseTickOption(line);
			}
			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg().desc( "Ticks to the simulator’s main loop (default value is 10).").build());
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc( "Select between gui or console mode (Default is gui)").build());
		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}
	
	private static void parseModeOption(CommandLine line) {
		_mode = line.getOptionValue("m");
		if(_mode.equals(null)) {
			_mode = "gui";
		}
	}
	
	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if((_mode.equals("console"))&& (_inFile == null)) {
			throw new ParseException("An events file is missing");
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}
	
	private static void parseTickOption(CommandLine line) throws ParseException {
		String ticks;
		ticks = line.getOptionValue("t");
		if(ticks == null) {
			_ticks = _timeLimitDefaultValue;
		}
		else {
			_ticks = Integer.parseInt(ticks);
		}
	}
	
	private static void initFactories() {
		List<Builder<LightSwitchingStrategy>> listSwitch = new ArrayList<>();
		listSwitch.add(new RoundRobinStrategyBuilder("round_robin_lss"));
		listSwitch.add(new MostCrowdedStrategyBuilder("most_crowded_lss"));
		
		List<Builder<DequeuingStrategy>> listDequeue = new ArrayList<>();
		listDequeue.add(new MoveFirstStrategyBuilder("move_first_dqs"));
		listDequeue.add(new MoveAllStrategyBuilder("most_all_dqs"));
		
		Factory<LightSwitchingStrategy> SwitchFactory = new BuilderBasedFactory<>(listSwitch);
		Factory<DequeuingStrategy> DequeueFactory = new BuilderBasedFactory<>(listDequeue);
		
		List<Builder<Event>> eventBuilder = new ArrayList<>();
		eventBuilder.add(new NewVehicleEventBuilder("new_vehicle"));
		eventBuilder.add(new NewCityRoadEventBuilder("new_city_road"));
		eventBuilder.add(new NewInterCityRoadEventBuilder("new_inter_city_road"));
		eventBuilder.add(new NewJunctionEventBuilder("new_junction",SwitchFactory,DequeueFactory));
		eventBuilder.add(new SetContClassEventBuilder("set_cont_class"));
		eventBuilder.add(new SetWeatherEventBuilder("set_weather"));
		
		_eventsFactory = new BuilderBasedFactory<>(eventBuilder);
	}

	private static void startBatchMode() throws IOException {
		TrafficSimulator traffic = new TrafficSimulator();
		Controller cont = new Controller(traffic,_eventsFactory);
		
		File initialFile = new File(_inFile);
		InputStream targetStream = new FileInputStream(initialFile);
		
		if(_outFile == null) {
			cont.loadEvents(targetStream);
			cont.run(_ticks, null);
		}
		else {
			File outFile = new File(_outFile);
			OutputStream outnow = new FileOutputStream(outFile);
			
			cont.loadEvents(targetStream);
			cont.run(_ticks, outnow);
		}
	}
	
	private static void startGuiMode() throws IOException {
		TrafficSimulator traffic = new TrafficSimulator();
		Controller cont = new Controller(traffic,_eventsFactory);
		
		if(_inFile != null) {
			File initialFile = new File(_inFile);
			InputStream targetStream = new FileInputStream(initialFile);
			cont.loadEvents(targetStream);
			//cont.run(_ticks, null);
		}
		
		else {
			
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			new MainWindow(cont);
			}
		});
	}

	private static void start(String[] args) throws IOException {
		initFactories();
		parseArgs(args);
		if(_mode.equals("console")) {
		startBatchMode();
		}
		else if(_mode.equals("gui")) {
		startGuiMode();
		}
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
