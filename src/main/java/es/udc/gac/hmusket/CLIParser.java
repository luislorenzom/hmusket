package es.udc.gac.hmusket;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import es.udc.gac.hmusket.exception.FileInputTypeNotFoundException;

public class CLIParser {

	public static String parse(String[] args) {

		// Create the command line parser
		CommandLineParser parser = new DefaultParser();

		// Create options
		Options options = new Options();

		options.addOption(Option.builder("k").argName("int/unit").hasArg()
				.desc("Specify two paramters: k-mer size and estimated total number of k-mers for this k-mer size)")
				.build());

		options.addOption(Option.builder("o").argName("str").hasArg().desc("The single output file name").build());

		options.addOption(Option.builder("omulti").argName("str").hasArg()
				.desc("Prefix of output file names, one input corresponding one output").build());

		options.addOption(
				Option.builder("p").argName("int").hasArg().desc("Number of threads [>=2], default=2").build());

		options.addOption(
				Option.builder("zlib").argName("int").hasArg().desc("Zlib-compressed output, default=0").build());

		options.addOption(Option.builder("maxtrim").argName("int").hasArg()
				.desc("Maximal number of bases that can be trimmed, default=0").build());

		options.addOption(
				Option.builder("inorder").desc("Keep sequences outputed in the same order with the input").build());

		options.addOption(Option.builder("lowercase").desc("Write corrected bases in lowercase, default=0").build());

		options.addOption(Option.builder("maxbuff").argName("int").hasArg()
				.desc("Capacity of message buffer for each worker, default=1024").build());

		options.addOption(Option.builder("multik").argName("bool").hasArg()
				.desc("Enable the use of multiple k-mer sizes, default=0").build());

		options.addOption(Option.builder("maxerr").argName("int").hasArg()
				.desc("Maximal number of mutations in any region of length #k, default=4").build());

		options.addOption(Option.builder("maxiter").argName("int").hasArg()
				.desc("Maximal number of correcting iterations per k-mer size, default=2").build());

		options.addOption(Option.builder("minmulti").argName("int").hasArg()
				.desc("Minimum multiplicty for correct k-mers [only applicable when not using multiple k-mer sizes], default=0")
				.build());

		options.addOption(Option.builder("fileIn").argName("filePath").hasArg().required().numberOfArgs(2)
				.desc("File where there are the sequences").build());

		options.addOption(Option.builder("fileOut").argName("filePath").hasArg().required()
				.desc("File where there want to save the output").build());

		options.addOption(Option.builder("fileType").argName("a/q").hasArg().required()
				.desc("File type <a> for FASTA files and <q> for FASTQ files").build());

		options.addOption(Option.builder("pairEnd").desc("If the input files are in pair end format").build());

		// Check the arguments received and create the "query" to pass it to
		// Musket
		String arguments = "";

		try {
			CommandLine line = parser.parse(options, args);
			if (line != null) {

				if (line.hasOption("fileIn")) {
					String valueAssociate = line.getOptionValue("fileIn");
					if (valueAssociate != null) {
						HMusket.fileIn.add(valueAssociate);
						arguments += valueAssociate + "_local";
					}
				}

				if (line.hasOption("fileOut")) {
					String valueAssociate = line.getOptionValue("fileOut");
					if (valueAssociate != null) {
						HMusket.fileOut = valueAssociate;
						arguments += " -o " + valueAssociate;
					}
				}

				if (line.hasOption("fileType")) {
					String valueAssociate = line.getOptionValue("fileType");
					if (valueAssociate != null
							&& (valueAssociate.equalsIgnoreCase("q") || valueAssociate.equalsIgnoreCase("a"))) {
						HMusket.fileType = valueAssociate;
					} else {
						throw new FileInputTypeNotFoundException();
					}
				}

				if (line.hasOption("pairEnd")) {
					HMusket.pairEnd = Boolean.TRUE;
				}

				if (line.hasOption("k")) {
					String valueAssociate = line.getOptionValue("k");
					Integer.parseInt(valueAssociate);
					arguments += " -k " + valueAssociate;
				}

				if (line.hasOption("omulti")) {
					String valueAssociate = line.getOptionValue("omulti");
					if (valueAssociate != null) {
						arguments += " -omulti " + valueAssociate;
					}
				}

				if (line.hasOption("p")) {
					String valueAssociate = line.getOptionValue("p");
					Integer.parseInt(valueAssociate);
					arguments += " -p " + valueAssociate;
				}

				if (line.hasOption("zlip")) {
					String valueAssociate = line.getOptionValue("zlip");
					Integer.parseInt(valueAssociate);
					arguments += " -zlip " + valueAssociate;
				}

				if (line.hasOption("maxtrim")) {
					String valueAssociate = line.getOptionValue("maxtrim");
					Integer.parseInt(valueAssociate);
					arguments += " -maxtrim " + valueAssociate;
				}

				if (line.hasOption("inorder")) {
					String valueAssociate = line.getOptionValue("inorder");
					if (valueAssociate != null) {
						arguments += " -inorder " + valueAssociate;
					}
				}

				if (line.hasOption("lowercase")) {
					String valueAssociate = line.getOptionValue("lowercase");
					if (valueAssociate != null) {
						arguments += " -lowercase " + valueAssociate;
					}
				}

				if (line.hasOption("maxbuff")) {
					String valueAssociate = line.getOptionValue("maxbuff");
					Integer.parseInt(valueAssociate);
					arguments += " -maxbuff " + valueAssociate;
				}

				if (line.hasOption("multik")) {
					String valueAssociate = line.getOptionValue("multik");
					if (valueAssociate != null) {
						arguments += " -multik " + valueAssociate;
					}
				}

				if (line.hasOption("maxerr")) {
					String valueAssociate = line.getOptionValue("maxerr");
					Integer.parseInt(valueAssociate);
					arguments += " -maxerr " + valueAssociate;
				}

				if (line.hasOption("maxiter")) {
					String valueAssociate = line.getOptionValue("maxiter");
					Integer.parseInt(valueAssociate);
					arguments += " -maxiter " + valueAssociate;
				}

				if (line.hasOption("minmulti")) {
					String valueAssociate = line.getOptionValue("minmulti");
					Integer.parseInt(valueAssociate);
					arguments += " -minmulti " + valueAssociate;
				}

			}

		} catch (ParseException | NumberFormatException | FileInputTypeNotFoundException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("hmusket", "-------------------------------------------------------------------",
					options, null, true);
			System.exit(0);
		}
		return arguments;
	}
}
