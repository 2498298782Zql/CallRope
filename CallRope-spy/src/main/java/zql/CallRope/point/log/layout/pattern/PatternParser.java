package zql.CallRope.point.log.layout.pattern;

import zql.CallRope.point.log.exception.ConfigException;
import zql.CallRope.point.log.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatternParser {
    enum ParserState {
        //字符
        LITERAL_STATE,

        KEYWORD_STATE
    }

    public static final char PERCENT_CHAR = '%';

    private String pattern;

    public static final Map<String, Class> defaultConverterMap = new HashMap<>();

    public PatternParser(String pattern) {
        this.pattern = pattern;
    }


    static {
        //put converters to defaultConverterMap
        defaultConverterMap.put("d", DateConverter.class);
        defaultConverterMap.put("t", ThreadConverter.class);
        defaultConverterMap.put("m", MessageConverter.class);
        defaultConverterMap.put("p", LevelConverter.class);
        defaultConverterMap.put("c", LoggerConverter.class);
        defaultConverterMap.put("n", LineSeparatorConverter.class);
    }


    public List<Node> parse() {
        List<Node> nodes = new ArrayList<>();
        int i = 0;
        char c;
        ParserState state = ParserState.LITERAL_STATE;
        StringBuilder literalBuf = new StringBuilder();
        int patternLength = pattern.length();
        while (i != patternLength - 1) {
            c = pattern.charAt(i++);
            switch (state) {
                case LITERAL_STATE:
                    if (c == PERCENT_CHAR) {
                        if (literalBuf.length() > 0) {
                            Node node = new Node(Node.LITERAL, literalBuf.toString());
                            nodes.add(node);
                            literalBuf.setLength(0);
                        }
                        state = ParserState.KEYWORD_STATE;
                    }
                    literalBuf.append(c);
                    break;
                case KEYWORD_STATE:
                    if (!Character.isJavaIdentifierPart(c)) {
                        KeywordNode node = new KeywordNode(literalBuf.toString());
                        nodes.add(node);
                        state = ParserState.LITERAL_STATE;
                        literalBuf.setLength(0);
                    }
                    literalBuf.append(c);
                    break;
            }
        }

        compileNode(nodes);
        return nodes;
    }

    private void compileNode(List<Node> nodes) {
        for (Node n : nodes) {
            Converter converter = null;
            if (n instanceof KeywordNode) {
                String keyword = ((KeywordNode) n).getKeyword();
                Class clazz = defaultConverterMap.get(keyword);
                if (clazz == null) {
                    throw new ConfigException("pattern[%" + keyword + "] illegal!");
                }
                try {
                    converter = (Converter) ReflectionUtils.newInstance(clazz);
                } catch (Exception e) {
                    throw new ConfigException(e);
                }
            } else {
                converter = new LiteralConverter(n.getValue());
            }
            n.converter = converter;
        }
    }

    private Node newHead(Node head, Node node) {
        if (head == null) {
            head = node;
        } else {
            head.next = node;
        }
        return head;
    }
}
