package es.udc.gac.hmusket;

import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import es.udc.gac.hmusket.exception.FileInputTypeNotFoundException;
import es.udc.gac.hmusket.exception.PairEndWithoutTwoDatasetsException;

public class CLIParser {

	public static String parse(String[] args) {

		// Create the command line parser
		CommandLineParser parser = new DefaultParser();

		// Create options
		Options options = new Options();

		options.addOption(Option.builder("k").argName("int/unit").hasArg()
				.desc("Specify two paramters: k-mer size and estimated total number of k-mers for this k-mer size)")
				.build());

		options.addOption(
				Option.builder("p").argName("int").hasArg().desc("Number of threads [>=2], default=2").build());

		options.addOption(
				Option.builder("zlib").argName("int").hasArg().desc("Zlib-compressed output, default=0").build());

		options.addOption(Option.builder("maxtrim").argName("int").hasArg()
				.desc("Maximal number of bases that can be trimmed, default=0").build());

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

		options.addOption(Option.builder("fileIn").argName("filePath").hasArg().required()
				.numberOfArgs(Option.UNLIMITED_VALUES).desc("Dataset's path").build());

		options.addOption(Option.builder("fileOut").argName("filePath").hasArg().required()
				.desc("The single output file name").build());

		options.addOption(Option.builder("localCopyPath").argName("filePath").hasArg().required()
				.desc("Where is going to be copy the dataset's chunk in the compute node").build());

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
					String[] valueAssociate = line.getOptionValues("fileIn");
					if (valueAssociate != null) {
						HMusket.fileIn = Arrays.asList(valueAssociate);
					}
				}

				if (line.hasOption("fileType")) {
					String valueAssociate = line.getOptionValue("fileType");
					if (valueAssociate != null
							&& (valueAssociate.equalsIgnoreCase("q") || valueAssociate.equalsIgnoreCase("a"))) {
						HMusket.fileType = valueAssociate;
					} else {
						throw new FileInputTypeNotFoundException(line.getOptionValue("fileType"));
					}
				}

				if (line.hasOption("localCopyPath")) {
					String valueAssociate = line.getOptionValue("localCopyPath");
					if (valueAssociate != null) {
						String localCopyPath = CLIParser.getLocalCopyPath(valueAssociate);
						HMusket.localCopyPath = localCopyPath;
						arguments += "musket " + localCopyPath;
					}
				}

				if (line.hasOption("fileOut")) {
					String valueAssociate = line.getOptionValue("fileOut");
					if (valueAssociate != null) {
						HMusket.fileOut = valueAssociate;
						arguments += " -o " + valueAssociate + "_musket-output-"
								+ String.valueOf((System.currentTimeMillis()));
					}
				}

				if (line.hasOption("pairEnd")) {
					HMusket.pairEnd = Boolean.TRUE;

					String[] fileInValues = line.getOptionValues("fileIn");

					if (fileInValues.length != 2) {
						throw new PairEndWithoutTwoDatasetsException();
					}

					String fileOutputPath = line.getOptionValue("fileOut") + "_musket-output-"
							+ String.valueOf((System.currentTimeMillis()));
					// FIXME
					HMusket.localLeftCopyPath = HMusket.localCopyPath;
					HMusket.localRightCopyPath = HMusket.localCopyPath;

					arguments = "musket -omulti " + fileOutputPath + " -inorder " + HMusket.localLeftCopyPath + " "
							+ HMusket.localRightCopyPath;
				}

				if (line.hasOption("k")) {
					String valueAssociate = line.getOptionValue("k");
					Integer.parseInt(valueAssociate);
					arguments += " -k " + valueAssociate;
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

		} catch (ParseException | NumberFormatException e) {
			CLIParser.showHelp(options);
		} catch (FileInputTypeNotFoundException | PairEndWithoutTwoDatasetsException e) {
			// Custom error message
			System.err.println(e.getMessage());
			CLIParser.showHelp(options);
		}
		return arguments;
	}

	private static void showHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("hmusket", "-------------------------------------------------------------------", options,
				null, true);
		System.exit(0);
	}

	private static String getLocalCopyPath(String folder) {

		String localPath = folder;

		// Checks the last char of localCopyPath
		if (!folder.substring(folder.length() - 1).equals("/")) {
			localPath += "/";
		}

		// Creates file's name
		String fileName = HMusket.applicationName + "_" + String.valueOf((System.currentTimeMillis()));

		if (HMusket.fileType.equalsIgnoreCase("a")) {
			fileName += fileName + ".fasta";
		} else if (HMusket.fileType.equalsIgnoreCase("q")) {
			fileName += fileName + ".fastq";
		}

		// Concats and returns it
		return localPath + fileName;
	}
}
