package gui;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import Searcher.Indexer;
import Searcher.Searcher;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.TitledBorder;
import javax.swing.JSeparator;
import javax.swing.JComboBox;

/***
 *
 * @author Sipitria, Valacco, Zamora
 *
 */

public class TPSearcher extends JFrame {

	private static TPSearcher frame;
	private JPanel 	  container;
	private JTextArea logIndex;
	private JTextArea txtThreadPathI;
	private JTextArea txtIndexPathI;
	private JTextArea txtSearchB;
	private JTextArea txtIndexPathB;
	private JTextArea result;
	private JComboBox<String> seleccionCampo;
	
	
	public TPSearcher() {
		
//----------------------
// INTERFACE CONSTRUCTOR
//----------------------
		setTitle("Searcher");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 660, 590);
		
	//-----------
	// CONTENEDOR
	//-----------
		container = new JPanel(null);
		container.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(container);
		
	//------------
	// TABBED PANE
	//------------
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 647, 561);
		container.add(tabbedPane);
		
		
//----------------------------------------		
//--------------  INDEXADOR  -------------
//----------------------------------------
 		JPanel indexPane = new JPanel();
		tabbedPane.addTab("Indexador", null, indexPane, null);
		
	//-----		
	// LOG
	//-----
		JScrollPane panel = new JScrollPane();
		panel.setBorder(new TitledBorder(null, "Log", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		panel.setBounds(0, 118, 643, 404);
		panel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		indexPane.add(panel);
		
		logIndex = new JTextArea();
		logIndex.setEditable(false);
		logIndex.setBounds(10, 22, 623, 263);
		panel.setViewportView(logIndex);
		
	//-------
	// LABELS
	//-------
		JLabel lblThreadsI = new JLabel("Directorio de threads:");
		lblThreadsI.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblThreadsI.setBounds(10, 54, 128, 14);
		indexPane.add(lblThreadsI);
		
		JLabel lblIndexI = new JLabel("Directorio de indice:");
		lblIndexI.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblIndexI.setBounds(10, 14, 128, 14);
		indexPane.add(lblIndexI);
		
	//----------
	// TEXT AREA
	//----------
		txtThreadPathI = new JTextArea();
		txtThreadPathI.setFont(new java.awt.Font("Arial",1,12));
		txtThreadPathI.setBounds(140, 51, 388, 19);
		indexPane.add(txtThreadPathI);
		
		txtIndexPathI = new JTextArea();
		txtIndexPathI.setFont(new java.awt.Font("Arial",1,12));
		txtIndexPathI.setBounds(140, 11, 388, 19);
		indexPane.add(txtIndexPathI);
	
	//--------
	// BUTTONS
	//--------
		JButton btnThreadSearchI = new JButton("Examinar");
		btnThreadSearchI.setBounds(538, 50, 97, 23);
		indexPane.add(btnThreadSearchI);
		btnThreadSearchI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser chooser = new JFileChooser();
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Directorio Threads");
			    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    
			    if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { 
			        txtThreadPathI.setText(chooser.getSelectedFile().toString());
			    }
			}
		});
		
		JButton btnIndexSearchI = new JButton("Examinar");
		btnIndexSearchI.setBounds(538, 10, 97, 23);
		indexPane.add(btnIndexSearchI);
		btnIndexSearchI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser chooser = new JFileChooser();
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Directorio Threads");
			    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    
			    if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { 
			        txtIndexPathI.setText(chooser.getSelectedFile().toString());
			        txtIndexPathB.setText(chooser.getSelectedFile().toString());
			        
			    }
			}
		});
		
		JButton btnIndexar = new JButton("Indexar");
		btnIndexar.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnIndexar.setBounds(519, 84, 116, 28);
		indexPane.add(btnIndexar);
		btnIndexar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String xmlPath=txtThreadPathI.getText();
				String indexPath=txtIndexPathI.getText();
				
				//Creacion del indice a partir del dataset
				Indexer indexer = new Indexer();
				indexer.index(xmlPath,indexPath);
				
				logIndex.setText(indexer.getLog());
			}
		});
		
		
//---------------------------------------		
//--------------  BUSCADOR  -------------
//---------------------------------------
		JPanel searcherPane = new JPanel();
		tabbedPane.addTab("Buscador", null, searcherPane, null);
		searcherPane.setLayout(null);

	//-------
	// LABELS
	//-------
		JLabel lblIndexB = new JLabel("Directorio de indice:");
		lblIndexB.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblIndexB.setBounds(10, 486, 124, 14);
		searcherPane.add(lblIndexB);

	//----------
	// TEXT AREA
	//----------
		txtSearchB = new JTextArea();
		txtSearchB.setFont(new java.awt.Font("Arial",1,12));
		txtSearchB.setBounds(121, 11, 370, 23);
		searcherPane.add(txtSearchB);

		txtIndexPathB = new JTextArea();
		txtIndexPathB.setFont(new java.awt.Font("Arial",1,12));
		txtIndexPathB.setBounds(132, 483, 388, 19);
		searcherPane.add(txtIndexPathB);

	//--------
	// BUTTONS
	//--------	
		JButton btnSearch = new JButton("Buscar");
		btnSearch.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnSearch.setBounds(501, 9, 132, 28);
		searcherPane.add(btnSearch);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Searcher searcher = new Searcher();
				searcher.setQuery(seleccionCampo.getSelectedItem().toString(), txtSearchB.getText());
				TopDocs hits = searcher.search(txtIndexPathB.getText(), 100);
				for (ScoreDoc sd : hits.scoreDocs) {
					result.append(searcher.doc(sd.doc).get("ThreadID")+": "+sd.score+"  \n");
					
				//TopDocs hits = searcher.search(indexPath, 10,q);
				}
			}
		});
		
		JButton btnIndexSearchB = new JButton("Examinar");
		btnIndexSearchB.setBounds(530, 482, 97, 23);
		searcherPane.add(btnIndexSearchB);
		btnIndexSearchB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser chooser = new JFileChooser();
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Directorio Threads");
			    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    
			    if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { 
			        txtIndexPathB.setText(chooser.getSelectedFile().toString());   
			    }
			}
		});
		
	//-------------
	// SEARCH PANEL
	//-------------
		JScrollPane panelB = new JScrollPane();
		panelB.setBorder(new TitledBorder(null, "Resultados", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		panelB.setBounds(0, 45, 643, 409);
		panelB.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		panelB.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		searcherPane.add(panelB);
		
		result = new JTextArea();
		result.setEditable(false);
		result.setBounds(10, 22, 623, 263);
		panelB.setViewportView(result);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 465, 623, 2);
		searcherPane.add(separator);
		
	//---------
	// COMBOBOX
	//---------
		String[] componentBox = {"Title", "Content", "ThreadID"};//este string es para inicializar el combobox
		seleccionCampo = new JComboBox<String>(componentBox);
		seleccionCampo.setBounds(10, 11, 101, 20);
		searcherPane.add(seleccionCampo);
		
		
//----------------------------------------		
//--------------  EVALUADOR  -------------
//----------------------------------------
		JPanel evaluatorPane = new JPanel();
		tabbedPane.addTab("Evaluador", null, evaluatorPane, null);
		evaluatorPane.setLayout(null);
		indexPane.setLayout(null);	
		
	}
	
	
	
	public static void main(String[] args) {
		
		frame = new TPSearcher();
		frame.setVisible(true);
		/*// TODO Auto-generated method stub
//		String xmlPath="F:\\Facultad\\Optativas\\Analisis y recuperacion de informacion\\Forum_Data\\All";
		String indexPath="index";
		/*
		//Creacion del indice a partir del dataset
		Indexer indexer = new Indexer();
		indexer.index(xmlPath,indexPath);
		
		//Creacion de los queries
		//Query q=new TermQuery(new Term("Content", "Ubuntu"));
		QueryParser p= new QueryParser("Content", new StandardAnalyzer());
		Query q=null;
		q = new TermQuery(new Term("contents", "lucene"));
		//TopDocs hits = is.search(q);
		try {
			q = p.parse("virtualbox keyboard problem");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("");
		System.out.println("----------------------------");
		System.out.println("");
		
		//Busqueda a partir del indice
		Searcher searcher = new Searcher();
		//TopDocs hits = searcher.search(indexPath, 100,q);
		
		TopDocs hits = searcher.search(indexPath, 10,q);
		
		System.out.println("Busqueda terminada");
		System.out.println("Cantidad de hits: " + hits.totalHits);
		
		for (ScoreDoc sd : hits.scoreDocs) {
			System.out.println(searcher.doc(sd.doc).get("ThreadID")+": "+sd.score+"   ");
		}
		try {
			Evaluator eval = new Evaluator("Forum_Data\\queryRelJudgements");
			eval.calculateRecall10k("virtualbox keyboard problem", hits, searcher);
			eval.calculatePrecision10k("virtualbox keyboard problem", hits, searcher);
			eval.calculateNDCG("virtualbox keyboard problem", hits, searcher);
			
			System.out.println("Recall: " + eval.getRecall10k());
			System.out.println("Precision: " + eval.getPrecision10k());
			System.out.println("NDCG: " + eval.getNdcg());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}
}
