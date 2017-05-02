import java.util.Scanner;
import java.util.*;

public class Problem
{
    public static void main(String[] args)
    {

       Scanner stdin = new Scanner(System.in);
       int n = Integer.parseInt(stdin.nextLine());
       ArrayList<String>input = new ArrayList<String>();

       while(stdin.hasNextLine()) 
       {    
           String line = stdin.nextLine();
           input.add(line);
       }
       
       List<List<String>> result = groupAnagrams(input);
       System.out.println(result.size());
       
    }
    
    public static List<List<String>> groupAnagrams(ArrayList<String> strs) {
      List<List<String>> result = new ArrayList<List<String>>();
 
      HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
      for(String str: strs){
            char[] arr = new char[27];
            for(int i=0; i<str.length(); i++){
                arr[str.charAt(i)-'a']++;
            }
           String ns = new String(arr);
 
          if(map.containsKey(ns)){
                map.get(ns).add(str);
          }else{
                ArrayList<String> al = new ArrayList<String>();
             al.add(str);
                map.put(ns, al);
         }
      }
    
     result.addAll(map.values());
 
      return result;
    }

}
