package vn.com.epi.gate.maltparser.benchmark;

import org.maltparser.MaltParserService;
import org.maltparser.core.exception.MaltChainedException;

public class Parser {

    private MaltParserService service = null;

    public Parser(String modelName) throws MaltChainedException {
        service = new MaltParserService();
        service.initializeParserModel("-c " + modelName
                + " -m parse -w . -lfi parser.log");
        service.terminateParserModel();
    }

    public String[] parser(String[] tokens) throws MaltChainedException {
        String[] outputTokens = null;
        if (service != null) {
            outputTokens = service.parseTokens(tokens);
            return outputTokens;
        } else {
            return null;
        }
    }
}
