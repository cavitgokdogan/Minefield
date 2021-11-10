import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Minefield implements MouseListener {
    JFrame frame;
    Button[][] board = new Button[10][10];
    int openButton;

    public Minefield(){
        openButton = 0;
        frame = new JFrame("Minefield");

        frame.setSize(800, 800);                    // Ekranda çıkacak kısmın boyutları
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(10,10));

        for (int row = 0; row < board.length; row++){
            for (int col = 0; col < board[0].length; col++){
                Button but = new Button(row, col);

                frame.add(but);
                but.addMouseListener(this);                       // Alt tarafta implement ettiğimiz için tekrardan onları buraya yazmaya gerek yok.
                board[row][col] = but;
            }
        }
        generateMine();
        updateCount();
        //printMine();
        frame.setVisible(true);                                 // Görünür hale getiriyoruz.
    }

    public void generateMine(){
        int i = 0;
        while (i < 10){
            int randRow = (int) (Math.random() * board.length);
            int randCol = (int) (Math.random() * board[0].length);

            while (board[randRow][randCol].isMine()){
                randRow = (int) (Math.random() * board.length);
                randCol = (int) (Math.random() * board[0].length);
            }
            board[randRow][randCol].setMine(true);
            i++;
        }
    }

    public void print(){
        for (Button[] buttons : board) {
            for (int col = 0; col < board[0].length; col++) {
                if (buttons[col].isMine()) {
                    buttons[col].setIcon(new ImageIcon("mine.png"));
                } else {
                    buttons[col].setText(buttons[col].getCount() + "");
                    buttons[col].setEnabled(false);
                }
            }
        }
    }

    public void printMine(){
        for (Button[] buttons : board) {
            for (int col = 0; col < board[0].length; col++) {
                if (buttons[col].isMine()) {
                    buttons[col].setIcon(new ImageIcon("mine.png"));
                }
            }
        }
    }

    public void updateCount(){
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col].isMine()) {
                    counting(row, col);
                }
            }
        }
    }

    public void counting(int row, int col){
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                try {
                    int value = board[i][j].getCount();
                    board[i][j].setCount(++value);
                }
                catch(Exception e){
                    // Hata vermesini engellemek için ekle..
                }
            }
        }
    }

    public void open(int ro, int co){
        if (ro < 0 || ro >= board.length || co < 0 || co >= board[0].length || board[ro][co].getText().length() > 0 || !board[ro][co].isEnabled()){
            return;
        }
        else if(board[co][ro].getCount() != 0){
            board[ro][co].setText(board[ro][co].getCount() + "");
            board[ro][co].setEnabled(false);
            openButton++;
        }
        else{
            openButton++;
            board[ro][co].setEnabled(false);
            open(ro - 1, co);
            open(ro + 1, co);
            open(ro, co - 1);
            open(ro, co + 1);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Button b = (Button) e.getComponent();
        if (e.getButton() == 1){            // Mouse da sol tıklandı.
            //System.out.println("Left Click....");     //Kontrol için ekle..
            if (b.isMine()){
                JOptionPane.showMessageDialog(frame, "You stepped on a mine. Game Over!!!");
                print();
            }
            else{
                open(b.getRow(), b.getCol());
                if (openButton == (board.length * board[0].length) - 10){
                    JOptionPane.showMessageDialog(frame, "Congratulations you won the game!!!");
                    print();
                }
            }
        }
        else if(e.getButton() == 3){        // Mouse da sağ tıklandı.
            //System.out.println("Right Click....");
            if (!b.isFlag()){
                b.setIcon(new ImageIcon("flag.png"));
                b.setFlag(true);
            }
            else{                           // 2. sağ tıkta koyulan bayrağı kaldırma bloğu
                b.setIcon(null);
                b.setFlag(false);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}