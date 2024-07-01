package zql.CallRope.point.log.layout.pattern;

public class KeywordNode extends Node {

    public KeywordNode(String value) {
        super(Node.KEYWORD,value);
    }

    public KeywordNode() {
    }

    public String getKeyword(){
        return value.substring(1);
    }
}