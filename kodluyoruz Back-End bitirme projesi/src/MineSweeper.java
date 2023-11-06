import java.util.Random;
import java.util.Scanner;

public class MineSweeper {

    private char[][] board;     // Oyun tahtası
    private  boolean[][] mines; // Mayınların konumları
    private int rows;            // Tahta satır sayısı
    private int cols;            // Tahta sütun sayısı
    private int remainingCells;  // Açılmamış hücre sayısı

    public MineSweeper(int rows,int cols){
        this.rows=rows;
        this.cols=cols;
        this.board=new char[rows][cols];
        this.mines=new boolean[rows][cols];
        this.remainingCells=rows * cols;
        initializeBoard();                  // Oyun tahtasını '-' karakteriyle doldur
        placeMines();                       // Mayınları rastgele yerleştir

    }
    // Oyun tahtasını '-' karakteri ile dolduran metot
   public void initializeBoard() {
        for (int i=0;i<rows; i++){
            for (int j=0; j<cols; j++){
                board[i][j]='-';
            }
        }
   }
    // Mayınları rastgele yerleştiren metot
    public void placeMines(){
        int numMines=remainingCells/4;  // Mayın sayısı, toplam hücre sayısının çeyreği
        Random ran=new Random();
        for ( int i = 0; i<numMines; i++){
            int row,col;
            do {
                row=ran.nextInt(rows);
                col=ran.nextInt(cols);
            }while ((mines[row][col]));
            mines[row][col]=true;
        }
    }
    // Oyun tahtasını ekranda gösteren metot
    public void displayBoard() {
        for (int i=0; i<rows; i++){
            for (int j=0; j<cols;j++){
                System.out.print(board[i][j]+ " ");
            }
            System.out.println();
        }
    }
    // Koordinatların geçerli olup olmadığını kontrol eden metot
    public boolean isValidCoordinate(int row,int col){
        return row>=0 && row<rows && col>=0 && col< cols;
    }
    // Oyun tahtasını mayınları göstererek kaybettikten sonra çağrılan metot
    public void revealBoard() {
       for (int i=0; i< rows;i++){
           for (int j=0; j<cols; j++){
               if (mines[i][j]){
                   board[i][j]='x';
               }
           }
       }
       displayBoard();
    }
    // Oyunu oynamak için kullanılan ana metot
    public void playGame(){
        Scanner scr=new Scanner(System.in);
        while (remainingCells > 0){
            displayBoard();
            System.out.print("Satır seçin( 0'dan başlayarak); ");
            int row=scr.nextInt();
            System.out.print("Sütun seçin( 0'dan başlayarak); ");
            int col=scr.nextInt();

            if (!isValidCoordinate(row,col) || board[row][col] !='-'){
                System.out.println("Geçersiz koordinat veya bu alan daha önce seçildi. Lütfen tekrar deneyin.");
                continue;
            }
            if (mines[row][col]){

                revealBoard();
                System.out.println("Oyunu kaybettiniz! Mayına bastınız. ");
                return;
            }else {
                int surroundingMines=countsurroundingMines(row,col);
                board[row][col]=(char) (surroundingMines +'0');
                remainingCells --;

                if (surroundingMines==0){
                    expandBlank(row,col);
                }
            }
        }
        System.out.println("Tebrikler! Tüm mayınları buldunuz ve oyunu kazandınız.");
        displayBoard();
    }
    // Bir hücrenin etrafındaki mayınları sayan metot
    public int countsurroundingMines(int row, int col){
        int count=0;
        for (int i=0; i<=1;i++){
          for (int j=0; j<= 1; j++){
              int newRow=row +i;
              int newCol =col +j;
              if (isValidCoordinate(newRow,newCol) && mines[newRow][newCol] ){
                count++;
              }
          }
        }
        return count;
    }
    // Boş bir hücreyi genişleten (açan) metot
    public void expandBlank(int row, int col){
        for (int i=-1; i<=1;i++){
            for (int j=-1; j<=1 ; j++){
                int newRow =row +i;
                int newCol =col +j;

                if (isValidCoordinate(newRow, newCol) && board[newRow][newCol] == '-') {
                    int surroundingMines = countsurroundingMines(newRow, newCol);
                    board[newRow][newCol] = (char) (surroundingMines + '0');
                    remainingCells--;

                    if (surroundingMines == 0) {
                        expandBlank(newRow, newCol);
                    }
                }
            }
        }
    }
}

