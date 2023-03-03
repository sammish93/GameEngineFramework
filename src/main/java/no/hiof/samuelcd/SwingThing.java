package no.hiof.samuelcd;
import javax.swing.*;

public class SwingThing {

    JFrame f;
    SwingThing(){

        f=new JFrame();

        JButton b=new JButton("click");
        b.setBounds(130,100,100, 40);

        f.add(b);

        f.setSize(400,500);//400 width and 500 height
        f.setLayout(null);//using no layout managers
        f.setVisible(true);//making the frame visible
    }

    public static void main(String[] args) {
        new SwingThing();
    }
}

