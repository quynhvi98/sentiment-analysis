package vn.com.epi.gate.maltparser;

import java.util.Iterator;

import org.maltparser.Engine;
import org.maltparser.MaltParserService;
import org.maltparser.core.exception.MaltChainedException;
import org.maltparser.core.flow.FlowChartInstance;
import org.maltparser.core.io.dataformat.ColumnDescription;
import org.maltparser.core.io.dataformat.DataFormatInstance;
import org.maltparser.core.options.OptionManager;
import org.maltparser.core.syntaxgraph.DependencyGraph;
import org.maltparser.core.syntaxgraph.DependencyStructure;
import org.maltparser.core.syntaxgraph.node.DependencyNode;
import org.maltparser.parser.SingleMalt;

public class ParserService extends MaltParserService {

    private int optionContainer;
    private Engine engine;
    private FlowChartInstance flowChartInstance;
    private SingleMalt singleMalt;
    private DataFormatInstance dataFormatInstance;
    private boolean initialized;

    /**
     * Creates a MaltParserService with the option container 0
     *
     * @throws MaltChainedException
     */
    public ParserService() throws MaltChainedException {
        super(0);
    }

    /**
     * Initialize a parser model that later can by used to parse sentences.
     * MaltParser is controlled by a commandLine string, please see the
     * documentation of MaltParser to see all available options.
     *
     * @param commandLine a commandLine string that controls the MaltParser
     * @throws MaltChainedException
     */
    @Override
    public void initializeParserModel(String commandLine)
            throws MaltChainedException {
        if (optionContainer == -1) {
            throw new MaltChainedException(
                    "MaltParserService has been initialized as an option free "
                            + "initialization and therefore no parser model can be initialized.");
        }
        OptionManager.instance().parseCommandLine(commandLine.split("\t"),
                optionContainer);
        // Creates an engine
        engine = new Engine();
        // Initialize the engine with option container and gets a flow chart
        // instance
        flowChartInstance = engine.initialize(optionContainer);
        // Runs the preprocess chart items of the "parse" flow chart
        if (flowChartInstance.hasPreProcessChartItems()) {
            flowChartInstance.preprocess();
        }
        singleMalt = (SingleMalt) flowChartInstance.getFlowChartRegistry(
                org.maltparser.parser.SingleMalt.class, "singlemalt");
        singleMalt.getConfigurationDir().initDataFormat();
        dataFormatInstance = singleMalt
                .getConfigurationDir()
                .getDataFormatManager()
                .getInputDataFormatSpec()
                .createDataFormatInstance(
                        singleMalt.getSymbolTables(),
                        OptionManager.instance().getOptionValueString(
                                optionContainer, "singlemalt", "null_value"));
        initialized = true;
    }

    /**
     * Parses an array of tokens and returns a dependency structure.
     * <p>
     * Note: To call this method requires that a parser model has been
     * initialized by using the initializeParserModel().
     *
     * @param tokens an array of tokens
     * @return a dependency structure
     * @throws MaltChainedException
     */
    @Override
    public DependencyStructure parse(String[] tokens)
            throws MaltChainedException {
        if (!initialized) {
            throw new MaltChainedException(
                    "No parser model has been initialized. Please use the method "
                            + "initializeParserModel() before invoking this method.");
        }
        if (tokens == null || tokens.length == 0) {
            throw new MaltChainedException("Nothing to parse. ");
        }

        DependencyStructure outputGraph = new DependencyGraph(
                singleMalt.getSymbolTables());

        for (int i = 0; i < tokens.length; i++) {
            Iterator<ColumnDescription> columns = dataFormatInstance.iterator();
            DependencyNode node = outputGraph.addDependencyNode(i + 1);
            String[] items = tokens[i].split("\t");
            for (int j = 0; j < items.length; j++) {
                if (columns.hasNext()) {
                    ColumnDescription column = columns.next();
                    if (column.getCategory() == ColumnDescription.INPUT
                            && node != null) {
                        outputGraph.addLabel(node, column.getName(), items[j]);
                    }
                }
            }
        }
        outputGraph.setDefaultRootEdgeLabel(outputGraph.getSymbolTables()
                .getSymbolTable("DEPREL"), "ROOT");
        // Invoke parse with the output graph
        singleMalt.parse(outputGraph);
        return outputGraph;
    }

    /**
     * Terminates the parser model.
     *
     * @throws MaltChainedException
     */
    @Override
    public void terminateParserModel() throws MaltChainedException {
        if (!initialized) {
            throw new MaltChainedException(
                    "No parser model has been "
                            + "initialized. Please use the method initializeParserModel() "
                            + "before invoking this method.");
        }
        // Runs the postprocess chart items of the "parse" flow chart
        if (flowChartInstance.hasPostProcessChartItems()) {
            flowChartInstance.postprocess();
        }

        // Terminate the flow chart with an option container
        engine.terminate(optionContainer);
    }

}
