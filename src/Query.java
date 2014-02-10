public class Query{
 
 public boolean initRequired = true;
 private static ArrayList<Query> allQueries;
 private ArrayList<QNode> query;
 private ArrayList<Integer> gameIds;
 private String qString;
 private NodeType OPERATOR = NodeType.OPERATOR, NON_DTD = NodeType.NON_DTD, STAT = NodeType.STAT,
    CHAMPION = NodeType.CHAMPION, ITEM = NodeType.ITEM, SUMMONER = NodeType.SUMMONER,
    TEAM = NodeType.TEAM, MODIFIER = NodeType.MODIFIER;
 private HashMap<String, NodeType> operators = new HashMap<String, NodeType>();
 
 public static void init(){
     operators.add("on", TEAM);
     operators.add("as", CHAMPION);
     operators.add("building", ITEM);
     operators.add("of", SUMMONER);
     operators.add("earned", MODIFIER);
     operators.add("by", MODIFIER);
     operators.add("earnedby", SUMMONER);
     operators.add("playing", MODIFIER);
     operators.add("with", MODIFIER);
     operators.add("against", MODIFIER);
     operators.add("playingwith", NON_DTD);
     operators.add("playingagainst", NON_DTD);
     
 }
 
 public Query(String s){
     qString = s;  
     for(String str: s.split("\\s"))
         query.add(new QNode(str));
     parse();
 }  
 
 private void parse(){
     //check for 
     for(int i=0; i<query.size(); i++){
         if(query.get(i).is() != OPERATOR)
             if(query.size() > i+1 && query.get(i+1).is() != OPERATOR){
                 merge(i, i+1);
                 parse();
             }
     }
     for(QNode q: query){
         
     }
 }
       
 private void merge(int i, int j){
     query.get(i) = query.get(i).concat(query.get(j));
     query.remove(j);
 }    
       
 private class QNode{
     
     private String word;
     private NodeType type;
     private QNode operator;
     
     public QNode(String s){
         type = NON_DTD;
         for(String t: operators){
             if(t.equals(s))
                type = OPERATOR;
         }
         if(type == OPERATOR)
         operator = this;
     }
     
     public boolean is(NodeType check){
         return type == check;
     }
     
 }
 
 private enum NodeType{
     NON_DTD, STAT, CHAMPION, ITEM, SUMMONER, OPERATOR, TEAM, MODIFIER;
 }
 
}  