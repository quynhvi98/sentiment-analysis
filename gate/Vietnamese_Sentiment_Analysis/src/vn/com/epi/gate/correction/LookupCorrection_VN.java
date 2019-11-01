package vn.com.epi.gate.correction;

import gate.*;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;
import gate.util.InvalidOffsetException;
import gate.util.persistence.PersistenceManager;
import vn.com.epi.gate.AnnotationOffsetComparator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Posted from May 25, 2018 1:43 AM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 */
public class LookupCorrection_VN extends AbstractLanguageAnalyser implements ProcessingResource {
    private static final long serialVersionUID = 1L;
    private static AnnotationOffsetComparator ANNOTATION_OFFSET_COMPARATOR = new AnnotationOffsetComparator();

    private Annotation[] tokens;
    private Annotation[] lookups;

    public static void main(String args[]) throws GateException, IOException {
        Gate.init();
        CorpusController controller = (CorpusController) PersistenceManager
                .loadObjectFromFile(new File("E://VNcorr.gapp"));
        controller.execute();
        LookupCorrection_VN correction = new LookupCorrection_VN();
        correction.setCorpus(controller.getCorpus());
        correction.init();
        for (int i = 0; i < correction.getCorpus().size(); i++) {
            Document doc = correction.getCorpus().get(i);
            correction.setDocument(doc);
            correction.execute();
        }
    }

    public Resource init() {
        return this;
    }

    public void reInit() throws ResourceInstantiationException {
        init();
    }

    public void execute() {
        preprocess();
        if (tokens == null || lookups == null) {
            return;
        }
        int leftIndex = 0;
        int rightIndex = 0;
        for (int i = 0; i < lookups.length; i++) {
            Annotation currLookup = lookups[i];
            leftIndex = findLeftMostTokens(leftIndex, currLookup);
            Annotation leftMostToken = tokens[leftIndex];
            rightIndex = findRightMostTokens(rightIndex, currLookup);
            Annotation rightMostToken = tokens[rightIndex];
            if (currLookup.getStartNode().getOffset().longValue() != leftMostToken
                    .getStartNode().getOffset().longValue()
                    || currLookup.getEndNode().getOffset().longValue() != rightMostToken
                    .getEndNode().getOffset().longValue()) {
                document.getAnnotations().remove(currLookup);
            } else {
                if ((rightIndex - leftIndex) != 0) {
                    if (lookups[i].getFeatures().get("majorType")
                            .equals("pro_known")) {
                        if (document.getAnnotations().get(
                                tokens[leftIndex].getId()) != null) {
                            for (int j = leftIndex; j <= rightIndex; j++) {
                                document.getAnnotations().remove(tokens[j]);
                            }
                            FeatureMap tokenFMap = Factory.newFeatureMap();
                            tokenFMap.put("kind", "Token");
                            tokenFMap.put("lookup", "pro_know");
                            tokenFMap.put("string",
                                    gate.Utils.stringFor(document, lookups[i]));
                            tokenFMap.put("leng", lookups[i].getEndNode()
                                    .getOffset()
                                    - lookups[i].getStartNode().getOffset());

                            int idNp = addOverlappingAnnotation(lookups[i]
                                            .getStartNode().getOffset(), lookups[i]
                                            .getEndNode().getOffset(), tokenFMap,
                                    "Token");
                        }
                    }
                }
            }
        }
    }

    public void preprocess() {
        AnnotationSet defaultAnnotationSet = document.getAnnotations();

        AnnotationSet tokenAnnotationSet = defaultAnnotationSet
                .get(TOKEN_ANNOTATION_TYPE);
        AnnotationSet lookupAnnotationSet = defaultAnnotationSet
                .get(LOOKUP_ANNOTATION_TYPE);
        if (tokenAnnotationSet == null || tokenAnnotationSet.isEmpty()
                || lookupAnnotationSet == null || lookupAnnotationSet.isEmpty()) {
            tokens = null;
            lookups = null;
        } else {
            tokens = tokenAnnotationSet
                    .toArray(new Annotation[tokenAnnotationSet.size()]);
            lookups = lookupAnnotationSet
                    .toArray(new Annotation[lookupAnnotationSet.size()]);
            Arrays.sort(tokens, ANNOTATION_OFFSET_COMPARATOR);
            Arrays.sort(lookups, ANNOTATION_OFFSET_COMPARATOR);
        }
    }

    public int findLeftMostTokens(int index, Annotation currLookup) {
        while (index < tokens.length
                && tokens[index].getStartNode().getOffset().longValue() <= currLookup
                .getStartNode().getOffset().longValue()) {
            index++;
        }
        return (index - 1 >= 0 ? index - 1 : 0);
    }

    public int findRightMostTokens(int index, Annotation currLookup) {
        while (index < tokens.length
                && tokens[index].getEndNode().getOffset().longValue() < currLookup
                .getEndNode().getOffset().longValue()) {
            index++;
        }
        if (index < tokens.length) {
            return index;
        } else {
            return tokens.length - 1;
        }
    }

    public Integer addOverlappingAnnotation(long start, long end,
                                            FeatureMap fMap, String type) {
        Integer id = null;
        AnnotationSet oldAnnotationSet = document.getAnnotations().get(type,
                start, end);
        if (oldAnnotationSet.size() == 0) {
            try {
                return document.getAnnotations().add(start, end, type, fMap);
            } catch (InvalidOffsetException e) {
                throw new RuntimeException("Impossible exception", e);
            }
        } else {
            id = oldAnnotationSet.iterator().next().getId();
        }
        return id;
    }

}
