// Подключение библиотек
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
public class Player1Frame implements ActionListener {
// В методе мейн создаем экземпляр класса
	public static void main(String[] args) {		
		new Player1Frame();
	}
	JFrame myFrame;
	JPanel myPanel1;
	JButton[][] myButtons;	// Массив кнопок
	JMenuBar myMenuBar;
	JMenu myFileMenu;
	JMenuItem myLoadMenuItem;
	int bombCounter, saveCounter;	// Счетчик бомб и открытых "чистых" ячеек
	int rnd;	// Случайное число
	ArrayList<Integer> bombList;	// Номера ячеек, где есть бомба
	ArrayList<Integer> sos;			// Номера соседних ячеек
	String temp;
	// Конструктор класса
	Player1Frame(){
		myFrame = new JFrame("Сапер");
		myMenuBar = new JMenuBar();
		myFileMenu = new JMenu("Файл");
		myLoadMenuItem = new JMenuItem("Новая игра");
		myMenuBar.add(myFileMenu);
		myFileMenu.add(myLoadMenuItem);
		myLoadMenuItem.addActionListener(new LoadMenuListener());	// Новая игра
		myFrame.setJMenuBar(myMenuBar);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setSize(800,600);
		mineCreate();	// Вызов метода заполнения минного поля
	}
	// Меотд заполнения минного поля
	public void mineCreate(){
		myFrame.getContentPane().removeAll(); 		// Удаляем содержимое фрейма
		myPanel1 = new JPanel();					// Создаем панель
		myPanel1.setLayout(new GridLayout(10,10));	// С разметкой в сетку
		myButtons = new JButton[10][10];			// Массив кнопок
		bombCounter=0;								// Обнуляем счетчик бомб
		saveCounter=0;								// И безопасных ячеек
		bombList = new ArrayList<Integer>();		// Список, где есть бомба
		while(bombCounter<10) {						// Пока не наберём необходимое кол-во бомб
			rnd=(int)(Math.random()*100);			// Берём случайное от 0 до 99
			System.out.print(rnd+" ");				// Проверка)
			if(!bombList.contains(rnd)) {			// Если в списке НЕТ такого адреса!
				bombList.add(rnd);					// То записываем его
				bombCounter++;						// И увеличиваем счетчик
			}
		}
		System.out.println();
		System.out.print(bombList.toString());
		System.out.println();
		for(int i=0;i<10;i++) {						// Создание кнопок, добавление им слушателя
			for(int j=0;j<10;j++) {					// И добавление на панель
				myButtons[i][j] = new JButton(i+""+j,new ImageIcon("f.jpg"));
				myButtons[i][j].addActionListener(this);
				myPanel1.add(myButtons[i][j]);
			}
		}
		myFrame.getContentPane().add(BorderLayout.CENTER, myPanel1);
		myFrame.setVisible(true);
	}
	// При нажатии на кнопку вызывается метод
	@Override
	public void actionPerformed(ActionEvent e) {
		openCell(Integer.parseInt(e.getActionCommand().toString().substring(0, 1)),Integer.parseInt(e.getActionCommand().toString().substring(1,2)));
	}
	// Метод открытия ячейки с параметрами i и j - индексы двумерного массива кнопок
	public void openCell(int loci, int locj) {
		temp=""+loci+locj;											// Через одно место приводим индексы к int как порядковый номер в диапазоне 0-99
		if(bombList.contains(Integer.parseInt(temp))){				// Если он есть в списке бомб, то конец(
			myButtons[loci][locj].setIcon(new ImageIcon("b.jpg"));
			gameOver();
		}
		else {
			saveCounter++;											// Иначе плюсуем удачное открытие
			sos = new ArrayList<Integer>();							// Создаем список для соседей
			int tl=110,tr=110,tu=110,td=110,tur=110,tul=110,tdr=110,tdl=110,sosco=0; // Соседи
			// НЕ ИСПОЛЬЗУЕТСЯ
			boolean center=false, left=false, right=false, up=false, down=false, upleft=false, upright=false, downleft=false, downright=false;
			// И проверяем условия, где открыта ячейка и сколько у нее соседей
			if(loci>0&&loci<9&&locj>0&&locj<9){
				tl=Integer.parseInt(""+(loci)+(locj-1));
				tr=Integer.parseInt(""+(loci)+(locj+1));
				tu=Integer.parseInt(""+(loci-1)+(locj));
				td=Integer.parseInt(""+(loci+1)+(locj));
				tur=Integer.parseInt(""+(loci-1)+(locj+1));
				tul=Integer.parseInt(""+(loci-1)+(locj-1));
				tdr=Integer.parseInt(""+(loci+1)+(locj+1));
				tdl=Integer.parseInt(""+(loci+1)+(locj-1));
				Collections.addAll(sos, tl,tr,tu,td,tur,tul,tdr,tdl);	// Записываем всех соседей в список
				center=true;
			}
			if(loci==0&&loci<9&&locj>0&&locj<9){
				tl=Integer.parseInt(""+(loci)+(locj-1));
				tr=Integer.parseInt(""+(loci)+(locj+1));
				td=Integer.parseInt(""+(loci+1)+(locj));
				tdr=Integer.parseInt(""+(loci+1)+(locj+1));
				tdl=Integer.parseInt(""+(loci+1)+(locj-1));
				Collections.addAll(sos, tl,tr,td,tdr,tdl);
				up=true;
			}
			if(loci>0&&loci==9&&locj>0&&locj<9){
				tl=Integer.parseInt(""+(loci)+(locj-1));
				tr=Integer.parseInt(""+(loci)+(locj+1));
				tu=Integer.parseInt(""+(loci-1)+(locj));
				tur=Integer.parseInt(""+(loci-1)+(locj+1));
				tul=Integer.parseInt(""+(loci-1)+(locj-1));
				Collections.addAll(sos, tl,tr,tu,tur,tul);
				down=true;
			}
			if(loci>0&&loci<9&&locj==0&&locj<9){
				tr=Integer.parseInt(""+(loci)+(locj+1));
				tu=Integer.parseInt(""+(loci-1)+(locj));
				td=Integer.parseInt(""+(loci+1)+(locj));
				tur=Integer.parseInt(""+(loci-1)+(locj+1));
				tdr=Integer.parseInt(""+(loci+1)+(locj+1));
				Collections.addAll(sos, tr,tu,td,tur,tdr);
				left=true;
			}
			if(loci>0&&loci<9&&locj>0&&locj==9){
				tl=Integer.parseInt(""+(loci)+(locj-1));
				tu=Integer.parseInt(""+(loci-1)+(locj));
				td=Integer.parseInt(""+(loci+1)+(locj));
				tul=Integer.parseInt(""+(loci-1)+(locj-1));
				tdl=Integer.parseInt(""+(loci+1)+(locj-1));
				Collections.addAll(sos, tl,tu,td,tul,tdl);
				right=true;
			}
			if(loci==0&&locj==0){
				tr=Integer.parseInt(""+(loci)+(locj+1));
				td=Integer.parseInt(""+(loci+1)+(locj));
				tdr=Integer.parseInt(""+(loci+1)+(locj+1));
				Collections.addAll(sos, tr,td,tdr);
				upleft=true;
			}
			if(loci==9&&locj==9){
				tl=Integer.parseInt(""+(loci)+(locj-1));
				tul=Integer.parseInt(""+(loci-1)+(locj-1));
				tu=Integer.parseInt(""+(loci-1)+(locj));
				Collections.addAll(sos, tl,tul,tu);
				upright=true;
			}
			if(loci==0&&locj==9){
				tl=Integer.parseInt(""+(loci)+(locj-1));
				tdl=Integer.parseInt(""+(loci+1)+(locj-1));
				td=Integer.parseInt(""+(loci+1)+(locj));
				Collections.addAll(sos, tl,td,tdl);
				downleft=true; 
			}
			if(loci==9&&locj==0){
				tu=Integer.parseInt(""+(loci-1)+(locj));
				tur=Integer.parseInt(""+(loci-1)+(locj+1));
				tr=Integer.parseInt(""+(loci)+(locj+1));
				Collections.addAll(sos, tr,tu,tur);
				downright=true;
			}
			System.out.println(saveCounter);
			System.out.println(Integer.parseInt(temp));
			for(int a : sos) {							// Проверяем кол-во бомб в соседних ячейках
				if(bombList.contains(a)) {
					sosco++;							// Кол-во бомб в соседних ячейках
				}
			}
			// В зависимости от кол-ва соседей устанавливаем картинку и деактивируем кнопку
			if(sosco==0){
				myButtons[loci][locj].setIcon(new ImageIcon("0.jpg"));
				myButtons[loci][locj].setEnabled(false);
			}
			if(sosco==1){
				myButtons[loci][locj].setIcon(new ImageIcon("1.jpg"));
				myButtons[loci][locj].setEnabled(false);
			}
			if(sosco==2){
				myButtons[loci][locj].setIcon(new ImageIcon("2.jpg"));
				myButtons[loci][locj].setEnabled(false);
			}
			if(sosco==3){
				myButtons[loci][locj].setIcon(new ImageIcon("3.jpg"));
				myButtons[loci][locj].setEnabled(false);
			}
			if(sosco==4){
				myButtons[loci][locj].setIcon(new ImageIcon("4.jpg"));
				myButtons[loci][locj].setEnabled(false);
			}
			if(sosco==5){
				myButtons[loci][locj].setIcon(new ImageIcon("5.jpg"));
				myButtons[loci][locj].setEnabled(false);
			}
			if(sosco==6){
				myButtons[loci][locj].setIcon(new ImageIcon("6.jpg"));
				myButtons[loci][locj].setEnabled(false);
			}
			if(sosco==7){
				myButtons[loci][locj].setIcon(new ImageIcon("7.jpg"));
				myButtons[loci][locj].setEnabled(false);
			}
			if(sosco==8){
				myButtons[loci][locj].setIcon(new ImageIcon("8.jpg"));
				myButtons[loci][locj].setEnabled(false);
			}
			if(saveCounter==90) {						// Условие победы
				myFrame.getContentPane().removeAll();
				ImageIcon icon = new ImageIcon("win.jpg"); 
				JLabel label = new JLabel();
				label.setIcon(icon);
				myFrame.getContentPane().add(BorderLayout.CENTER, label);
			}
		}
	}
	public void gameOver() {		// Проигрыш
		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++) {
				myButtons[i][j].setEnabled(false);		// Просто делаем все ячейки выкл
			}
		}
	}
	public class LoadMenuListener implements ActionListener{	// Кнопка новая игра
		public void actionPerformed(ActionEvent e){
			mineCreate();
		}
	}
}