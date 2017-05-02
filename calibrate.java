import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class calibrate {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileWriter writeFile=null; 
		BufferedWriter writer = null;
		int[][] outputBoard=new int[8][8];

		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				if((i==3 || i==4)&&(j==3 || j==4)){
					outputBoard[i][j]=3;
				}else{
					outputBoard[i][j]=0;
				}
				
			}
		}
		
		try {
			writeFile=new FileWriter("./previousBoard.txt");

			writer = new BufferedWriter(writeFile);
			//outputBoard=answerNode.stateBoard;
				for(int i=0;i<8;i++){
					for(int j=0;j<8;j++){
						if(outputBoard[i][j]==0){
							//System.out.print("*");
							writer.write("0");
						}else if(outputBoard[i][j]==1){
							//System.out.print("O");
							writer.write("3");
						}else{
							///System.out.print("X");
							writer.write("3");
						}
					}
					//System.out.println("");
					writer.newLine();
				}
				//printTraverseLog();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			writer.close();
			writeFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
