package gui;

import javax.swing.ImageIcon;
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
import evaluator.Evaluator;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.JSeparator;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Desktop;

/***
 *
 * @author Sipitria, Valacco, Zamora
 *
 */

public class TPSearcher extends JFrame {

	private static TPSearcher frame;
	private JPanel 	  		  container;
	private static JTextArea  logIndex;
	private JTextArea         txtThreadPathI;
	private JTextArea         txtIndexPathI;
	private JTextArea         txtIndexPathE;
	private JTextArea         txtSearchB;
	private JTextArea		  txtJudgmentPath;
	private JTextArea 		  txtIndexPathB;
	private JEditorPane	      result;
	private JComboBox<String> seleccionCampo;
	private JComboBox<String> seleccionQuery;
	private TopDocs 		  hits=null; 
	private Searcher 		  searcher;
	private JLabel 			  lblRecallValue;
	private JLabel    		  lblPrecitionValue; 
	private JLabel 			  lblNdcgValue; 

//	private JLabel lblSearchE;
	
	/**
	 * TODO LIST:
	 * 			-Terminar paginador, falta la funcionalidad la interfaz esta armada
	 * 			-Realizar search con hits y collector para recuperar todos los documentos posibles(sin limite)
	 * 			-Buscar imagen de flechas para botones next/prev page
	 * 
	 * 			-acomodar los datos que se mmuestran de cada documento recuperado, en buscador y en evaluador
	 * 			-mensajes de error en evaluador para cuando no se seleccionan paths
	 */
	
	public TPSearcher() {

		
		//----------------------
		// INTERFACE CONSTRUCTOR
		//----------------------
		setTitle("Searcher");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 653, 590);

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

		ImageIcon loading = new ImageIcon("img\\loader.gif");
		JLabel lblLoading = new JLabel("loading... ", loading, JLabel.CENTER);
		lblLoading.setBounds(250, 90, 128, 25);
		indexPane.add(lblLoading);
		lblLoading.setVisible(false);


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
				chooser.setDialogTitle("Directorio Indice");
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

				lblLoading.setVisible(true); 


				//Creacion del indice a partir del dataset
				Runnable miRunnable = new Runnable()
				{
					public void run()
					{
						lblLoading.repaint();
						Indexer indexer = new Indexer();
						indexer.index(xmlPath,indexPath);
						JOptionPane.showMessageDialog(null,"Fin del proceso de indexado");
						lblLoading.setVisible(false);
					}
				};

				Thread hilo = new Thread (miRunnable);
				hilo.start();


				lblLoading.repaint();
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

		txtIndexPathB = new JTextArea("");
		txtIndexPathB.setFont(new java.awt.Font("Arial",1,12));
		txtIndexPathB.setBounds(132, 483, 388, 19);
		searcherPane.add(txtIndexPathB);
		
		//-------------
		// SEARCH PANEL
		//-------------
		JScrollPane panelB = new JScrollPane();
		panelB.setBorder(new TitledBorder(null, "Resultados", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		panelB.setBounds(0, 45, 643, 386);
		panelB.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		panelB.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		searcherPane.add(panelB);
		
		result = new JEditorPane();
		result.setEditable(false);
		result.setBounds(10, 22, 623, 263);
		result.setContentType( "text/html" ); 
		result.addHyperlinkListener(new HyperlinkListener() {
		    public void hyperlinkUpdate(HyperlinkEvent hle) {
		    	if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {

		    		File xmlFile = new File (hle.getDescription().toString());
		    		try {
		    			Desktop.getDesktop().open(xmlFile);
		    		} catch (IOException e) {
		    			// TODO Auto-generated catch block
		    			e.printStackTrace();
		    		}
		    	}
		    }
		});
		
		panelB.setViewportView(result);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 465, 623, 2);
		searcherPane.add(separator);

		//--------
		// BUTTONS
		//--------	
	
		JButton btnSearch = new JButton("Buscar");
		btnSearch.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnSearch.setBounds(501, 9, 132, 28);
		searcherPane.add(btnSearch);
		result.setText("");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String txtResult="<html>\n";
				if (txtIndexPathB.getText().equals("")){
					JOptionPane.showMessageDialog(null,"Seleccione el directorio del indice");	
				}else{
					String searchText = txtSearchB.getText();
					//lblSearchE.setText(searchText);
					
					searcher = new Searcher();
					searcher.setQuery(seleccionCampo.getSelectedItem().toString(), searchText);
					hits = searcher.search(txtIndexPathB.getText(), 100);
					//hits = searcher.search(txtIndexPathB.getText(), 10);
					if (hits.totalHits == 0){
						txtResult=txtResult+"<font color=red size=+1>No se encontraron documentos relacionados con la busqueda</font>";
						JOptionPane.showMessageDialog(null,"Ning�n documento coincide con la busqueda");	
					}else{
						txtResult=txtResult+"<font size=+1 color=gray><b>Total de resultados: "+hits.totalHits+"</b></font><BR><BR>";
						String path;
						String titulo;
						
						for (ScoreDoc sd : hits.scoreDocs) {
							path=searcher.doc(sd.doc).get("Path");
							titulo=searcher.doc(sd.doc).get("Title");
							
							txtResult = txtResult + "<font size=+1><a href='" + path +"'> "+ titulo+ "</a></font>";
							txtResult = txtResult + "<font size=-1 color=gray>   "+sd.score+"</font><BR>";
							txtResult = txtResult + "<font size=-1 color=green>"+path+"</font><BR><BR>";
						}
					}
					txtResult=txtResult+"<html>\n";
					result.setText(txtResult);
					result.setCaretPosition(0);
					//
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


		//---------
		// COMBOBOX
		//---------
		String[] componentBox = {"Title", "Content", "ThreadID"};//este string es para inicializar el combobox
		seleccionCampo = new JComboBox<String>(componentBox);
		seleccionCampo.setBounds(10, 11, 101, 20);
		searcherPane.add(seleccionCampo);
		
		JLabel lblPageNumber = new JLabel("");
		lblPageNumber.setBounds(295, 438, 46, 18);
		searcherPane.add(lblPageNumber);
		
		JButton btnNextPage = new JButton("->");
		btnNextPage.setBounds(346, 436, 89, 23);
		searcherPane.add(btnNextPage);
		
		JButton btnPrevPage = new JButton("<-");
		btnPrevPage.setBounds(202, 436, 89, 23);
		searcherPane.add(btnPrevPage);
		indexPane.setLayout(null);	


		//----------------------------------------		
		//--------------  EVALUADOR  -------------
		//----------------------------------------

		JPanel evaluatorPane = new JPanel();
		tabbedPane.addTab("Evaluador", null, evaluatorPane, null);
		evaluatorPane.setLayout(null);

		JPanel recallPane = new JPanel();
		recallPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		recallPane.setBounds(10, 164, 332, 83);
		evaluatorPane.add(recallPane);
		recallPane.setLayout(null);

		JPanel docsPane = new JPanel();
		docsPane.setBorder(new TitledBorder(null, "Top 10 Docs", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		docsPane.setBounds(352, 155, 280, 342);
		evaluatorPane.add(docsPane);
		docsPane.setLayout(null);

		JPanel precitionPane = new JPanel();
		precitionPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		precitionPane.setLayout(null);
		precitionPane.setBounds(10, 258, 332, 83);
		evaluatorPane.add(precitionPane);
		
		JPanel ndcgPane = new JPanel();
		ndcgPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		ndcgPane.setLayout(null);
		ndcgPane.setBounds(10, 352, 332, 83);
		evaluatorPane.add(ndcgPane);

		//-------
		// LABELS
		//-------

		JLabel lblJudgments = new JLabel("Archivo de relevancia:");
		lblJudgments.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblJudgments.setBounds(10, 54, 128, 14);
		evaluatorPane.add(lblJudgments);

		JLabel lblRecall = new JLabel("Recall");
		lblRecall.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblRecall.setBounds(23, 31, 104, 24);
		recallPane.add(lblRecall);
		
		
		lblRecallValue = new JLabel("0.00000");
		lblRecallValue.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblRecallValue.setBounds(188, 29, 119, 29);
		recallPane.add(lblRecallValue);
		
		JLabel lblPrecition = new JLabel("Precisi\u00F3n");
		lblPrecition.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPrecition.setBounds(27, 30, 104, 24);
		precitionPane.add(lblPrecition);
		
		lblPrecitionValue = new JLabel("0.00000");
		lblPrecitionValue.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPrecitionValue.setBounds(188, 28, 119, 29);
		precitionPane.add(lblPrecitionValue);
		
		JLabel lblNdcg = new JLabel("nDCG");
		lblNdcg.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNdcg.setBounds(27, 33, 104, 24);
		ndcgPane.add(lblNdcg);
		
		lblNdcgValue = new JLabel("0.00000");
		lblNdcgValue.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNdcgValue.setBounds(178, 31, 144, 29);
		ndcgPane.add(lblNdcgValue);
		
		/*lblSearchE= new JLabel("");
		lblSearchE.setHorizontalAlignment(SwingConstants.CENTER);
		lblSearchE.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblSearchE.setBounds(125, 14, 379, 19);
		evaluatorPane.add(lblSearchE);*/
	
		JLabel lblNewLabel_1 = new JLabel("Directorio del indice");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1.setBounds(10, 14, 128, 14);
		evaluatorPane.add(lblNewLabel_1);

		//----------
		// TEXT AREA
		//----------


		txtJudgmentPath = new JTextArea();
		txtJudgmentPath.setFont(new java.awt.Font("Arial",1,12));
		txtJudgmentPath.setBounds(140, 51, 388, 19);
		evaluatorPane.add(txtJudgmentPath);
		
		txtIndexPathE = new JTextArea();
		txtIndexPathE.setFont(new java.awt.Font("Arial",1,12));
		txtIndexPathE.setBounds(140, 11, 388, 19);
		evaluatorPane.add(txtIndexPathE);

		JEditorPane txtTop10Docs = new JEditorPane();
		txtTop10Docs.setFont(new Font("Monospaced", Font.PLAIN, 15));
		txtTop10Docs.setText("");
		txtTop10Docs.setContentType( "text/html" );
		txtTop10Docs.setBounds(10, 17, 260, 318);
		docsPane.add(txtTop10Docs);
		
		//----------
		// BUTTONS
		//----------


		JButton btnCalculateMetrics = new JButton("Calcular M\u00E9tricas");
		btnCalculateMetrics.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnCalculateMetrics.setBounds(435, 100, 200, 25);
		evaluatorPane.add(btnCalculateMetrics);
			
		btnCalculateMetrics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				txtTop10Docs.setText("");
				
				lblNdcgValue.setText("0.00000");
				lblRecallValue.setText("0.00000");
				lblPrecitionValue.setText("0.00000");
				
				boolean boolIndex    = true;
				boolean boolJudgment = true;
				
				
				if (txtIndexPathE.getText().equals("")){
					boolIndex = false;
					JOptionPane.showMessageDialog(null,"Seleccione el directorio del indice");	
				}
				if (txtJudgmentPath.getText().equals("")){
					boolJudgment = false;
					JOptionPane.showMessageDialog(null,"Seleccione el archivo de direcciones");	
				}
				
				if (boolIndex && boolJudgment){
					String txtResult     = "<html>\n";
					String selectedQuery = seleccionQuery.getSelectedItem().toString();

					Searcher evalSearcher = new Searcher();
					
					evalSearcher.setQuery(seleccionCampo.getSelectedItem().toString(), selectedQuery);
					
					TopDocs evalHits = evalSearcher.search(txtIndexPathE.getText(), 10);
					Evaluator eval   = null;
					
					try {
						eval = new Evaluator(txtJudgmentPath.getText());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					eval.calculateRecall10k(selectedQuery, evalHits, evalSearcher);
					eval.calculatePrecision10k(selectedQuery, evalHits, evalSearcher);
					eval.calculateNDCG(selectedQuery, evalHits, evalSearcher);

					
					//txtResult=txtResult+"<font size=+1 color=gray><b>Total de resultados: "+evalhits.totalHits+"</b></font><BR><BR>";
					String path;
					String titulo;

					for (ScoreDoc sd : evalHits.scoreDocs) {
						path=evalSearcher.doc(sd.doc).get("Path");
						titulo=evalSearcher.doc(sd.doc).get("ThreadID");

						txtResult = txtResult + "<font size=+1><a href='" + path +"'> "+ titulo+ "</a></font>";
						txtResult = txtResult + "<font size=-1 color=gray>   "+eval.getRelevanceAt(selectedQuery,evalSearcher.doc(sd.doc).get("ThreadID")) +"</font><BR>";
						txtResult = txtResult + "<font size=-1 color=green>"+path+"</font><BR><BR>";
					}
					txtResult=txtResult+"<html>\n";
					txtTop10Docs.setText(txtResult);
					txtTop10Docs.setCaretPosition(0);
					
					/*for (ScoreDoc sd : evalHits.scoreDocs) {
						txtTop10Docs.append(evalSearcher.doc(sd.doc).get("ThreadID")+": "+sd.score+"  \n");
					}
					System.out.println("recall "+eval.getRecall10k());
				    System.out.println("precision "+eval.getPrecision10k());
				    System.out.println("nDCG "+eval.getNdcg());*/

					lblRecallValue.setText(Float.toString(eval.getRecall10k()));
					lblPrecitionValue.setText(Float.toString(eval.getPrecision10k()));
					lblNdcgValue.setText(Float.toString(eval.getNdcg()));
					//else mostrar ventana busqueda no realizada, 
				}
			}

		});


		JButton btnJudgementSel = new JButton("Examinar");
		btnJudgementSel.setBounds(538, 51, 97, 23);
		evaluatorPane.add(btnJudgementSel);
		btnJudgementSel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("seleccion Archivo de Relevancias");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

				if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { 
					txtJudgmentPath.setText(chooser.getSelectedFile().toString());   
				}
			}
		});
		
		JButton btnIndexSearchE = new JButton("Examinar");
		btnIndexSearchE.setBounds(538, 10, 97, 23);
		evaluatorPane.add(btnIndexSearchE);
		btnIndexSearchE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Directorio Indice");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { 
					txtIndexPathE.setText(chooser.getSelectedFile().toString());

				}
			}
		});


		//---------
		// COMBOBOX
		//---------
		String[] queryList = {
				"firefox no sound with flash",
				"virtualbox keyboard problem",
				"how to dual boot windows and ubuntu",
				"cisco vpn client for ubuntu",
				"how to adjust screen resolution in ubuntu",
				"running microsoft office with wine",
				"grub error while booting",
				"wireless connection not working",
				"gnome amarok alternative",
				"which is better gnome kde",
				"playing windows media files on ubuntu",
				"enable surround sound in ubuntu",
				"firefox crash with flash",
				"webcam not working in ubuntu",
				"mount ntfs partitions in ubuntu",
				"how to change file permissions",
				"how to set up a network printer",
				"dual monitor setup ubuntu",
				"burning dvd in ubuntu",
				"internal microphone not working",
				"dual boot ubuntu mac OS",
				"installing ubuntu from USB",
				"accessing ubuntu home in virtualbox windows",
				"mike not working with skype in ubuntu",
				"automount hard drive partitions"
		};//este string es para inicializar el combobox
		
		seleccionQuery = new JComboBox<String>(queryList);
		seleccionQuery.setBounds(10, 100, 414, 25);
		evaluatorPane.add(seleccionQuery);
		evaluatorPane.setLayout(null);	
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setToolTipText("");
		separator_1.setBounds(10, 79, 622, 4);
		evaluatorPane.add(separator_1);


	}

	// Metodo estatico para escribir en el log del indexador
	public static void indexLog(String s){
		logIndex.append(s+"\n");
		logIndex.repaint();
	}


	public static void main(String[] args) {

		frame = new TPSearcher();
		frame.setResizable(false);
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
		 */
		/*try {
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
