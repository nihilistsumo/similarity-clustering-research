package edu.unh.cs.treccar.proj.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.util.Resources;
import org.apache.pivot.util.concurrent.Task;
import org.apache.pivot.util.concurrent.TaskExecutionException;
import org.apache.pivot.util.concurrent.TaskListener;
import org.apache.pivot.wtk.ActivityIndicator;
import org.apache.pivot.wtk.Alert;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonGroup;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Checkbox;
import org.apache.pivot.wtk.FileBrowserSheet;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.ListView;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.Sheet;
import org.apache.pivot.wtk.SheetCloseListener;
import org.apache.pivot.wtk.TaskAdapter;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;

import edu.unh.cs.treccar.proj.similarities.DiceSimilarity;
import edu.unh.cs.treccar.proj.similarities.JaccardSimilarity;
import edu.unh.cs.treccar.proj.similarities.JiangConrathSimilarity;
import edu.unh.cs.treccar.proj.similarities.LeacockChodorowSimilarity;
import edu.unh.cs.treccar.proj.similarities.SimilarityFunction;
import edu.unh.cs.treccar.proj.util.ParaPairData;
import edu.unh.cs.treccar.proj.util.ProjectWorker;

public class BXMLHandler extends Window implements Bindable {
    //@BXML private ButtonGroup fileBrowserSheetModeGroup = null;
    @BXML private PushButton openSheetButton1 = null;
    @BXML private PushButton openSheetButton2 = null;
    @BXML private PushButton openSheetButton3 = null;
    @BXML private PushButton openSheetButton4 = null;
    @BXML private PushButton openSheetButton5 = null;
    @BXML private PushButton openSheetButton6 = null;
    @BXML private PushButton openSheetButton7 = null;
    @BXML private PushButton openSheetButton8 = null;
    @BXML private PushButton start = null;
    @BXML private TextInput input_out = null;
    @BXML private TextInput input_trp = null;
    @BXML private TextInput input_tep = null;
    @BXML private TextInput input_traq = null;
    @BXML private TextInput input_trhq = null;
    @BXML private TextInput input_teaq = null;
    @BXML private TextInput input_tehq = null;
    @BXML private TextInput input_simd = null;
    @BXML private TextInput score_data = null;
    @BXML private TextInput input_th = null;
    @BXML private Checkbox diceCheck = null, jacCheck = null, jiangCheck = null, leaCheck = null;
    
    final FileBrowserSheet fileBrowserSheet1 = new FileBrowserSheet();
    final FileBrowserSheet fileBrowserSheet2 = new FileBrowserSheet();
    final FileBrowserSheet fileBrowserSheet3= new FileBrowserSheet();
    final FileBrowserSheet fileBrowserSheet4 = new FileBrowserSheet();
    final FileBrowserSheet fileBrowserSheet5 = new FileBrowserSheet();
    final FileBrowserSheet fileBrowserSheet6 = new FileBrowserSheet();
    final FileBrowserSheet fileBrowserSheet7 = new FileBrowserSheet();
    final FileBrowserSheet fileBrowserSheet8 = new FileBrowserSheet();
    String outdir, trainPara, testPara, trainArt, trainHier, testArt, testHier, scoreData;
    ArrayList<SimilarityFunction> funcs;
    double threshold;
    public UIDataBinder data = null;
    String defaultDir = "/";
    private ActivityIndicator activityIndicator = null;
    

    @Override
    public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
    	activityIndicator = (ActivityIndicator)namespace.get("activityIndicator");
    	openSheetButton1.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {
                //final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();
                fileBrowserSheet1.setMode(FileBrowserSheet.Mode.SAVE_TO);
                fileBrowserSheet1.open(BXMLHandler.this, new SheetCloseListener() {
                    @Override
                    public void sheetClosed(Sheet sheet) {
                        if (sheet.getResult()) {
                        	//outdir = fileBrowserSheet.getRootDirectory().getAbsolutePath();
                        	outdir = fileBrowserSheet1.getSelectedFile().getAbsolutePath();
                        	defaultDir = outdir;
                        	input_out.setText(outdir);
                        } else {
                            Alert.alert(MessageType.INFO, "You didn't select anything.", BXMLHandler.this);
                        }
                    }
                });
            }
        });
        
        openSheetButton2.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {
                //final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();
                fileBrowserSheet2.setRootDirectory(fileBrowserSheet1.getRootDirectory());
                fileBrowserSheet2.setMode(FileBrowserSheet.Mode.SAVE_AS);
                fileBrowserSheet2.open(BXMLHandler.this, new SheetCloseListener() {
                    @Override
                    public void sheetClosed(Sheet sheet) {
                        if (sheet.getResult()) {
                        	trainPara = fileBrowserSheet2.getSelectedFile().getAbsolutePath();
                        	defaultDir = fileBrowserSheet2.getRootDirectory().getAbsolutePath();
                        	input_trp.setText(trainPara);
                        } else {
                            Alert.alert(MessageType.INFO, "You didn't select anything.", BXMLHandler.this);
                        }
                    }
                });
            }
        });
        
        openSheetButton3.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {
                //final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();
                fileBrowserSheet3.setRootDirectory(fileBrowserSheet2.getRootDirectory());
                fileBrowserSheet3.setMode(FileBrowserSheet.Mode.SAVE_AS);
                fileBrowserSheet3.open(BXMLHandler.this, new SheetCloseListener() {
                    @Override
                    public void sheetClosed(Sheet sheet) {
                        if (sheet.getResult()) {
                        	trainArt = fileBrowserSheet3.getSelectedFile().getAbsolutePath();
                        	defaultDir = fileBrowserSheet3.getRootDirectory().getAbsolutePath();
                        	input_traq.setText(trainArt);
                        } else {
                            Alert.alert(MessageType.INFO, "You didn't select anything.", BXMLHandler.this);
                        }
                    }
                });
            }
        });
        
        openSheetButton4.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {
                //final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();
                fileBrowserSheet4.setRootDirectory(fileBrowserSheet3.getRootDirectory());
                fileBrowserSheet4.setMode(FileBrowserSheet.Mode.SAVE_AS);
                fileBrowserSheet4.open(BXMLHandler.this, new SheetCloseListener() {
                    @Override
                    public void sheetClosed(Sheet sheet) {
                        if (sheet.getResult()) {
                        	trainHier = fileBrowserSheet4.getSelectedFile().getAbsolutePath();
                        	defaultDir = fileBrowserSheet4.getRootDirectory().getAbsolutePath();
                        	input_trhq.setText(trainHier);
                        } else {
                            Alert.alert(MessageType.INFO, "You didn't select anything.", BXMLHandler.this);
                        }
                    }
                });
            }
        });
        
        openSheetButton5.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {
                //final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();
                fileBrowserSheet5.setRootDirectory(fileBrowserSheet4.getRootDirectory());
                fileBrowserSheet5.setMode(FileBrowserSheet.Mode.SAVE_AS);
                fileBrowserSheet5.open(BXMLHandler.this, new SheetCloseListener() {
                    @Override
                    public void sheetClosed(Sheet sheet) {
                        if (sheet.getResult()) {
                        	testPara = fileBrowserSheet5.getSelectedFile().getAbsolutePath();
                        	defaultDir = fileBrowserSheet5.getRootDirectory().getAbsolutePath();
                        	input_tep.setText(testPara);
                        } else {
                            Alert.alert(MessageType.INFO, "You didn't select anything.", BXMLHandler.this);
                        }
                    }
                });
            }
        });
        
        openSheetButton6.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {
                //final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();
                fileBrowserSheet6.setRootDirectory(fileBrowserSheet5.getRootDirectory());
                fileBrowserSheet6.setMode(FileBrowserSheet.Mode.SAVE_AS);
                fileBrowserSheet6.open(BXMLHandler.this, new SheetCloseListener() {
                    @Override
                    public void sheetClosed(Sheet sheet) {
                        if (sheet.getResult()) {
                        	testArt = fileBrowserSheet6.getSelectedFile().getAbsolutePath();
                        	defaultDir = fileBrowserSheet6.getRootDirectory().getAbsolutePath();
                        	input_teaq.setText(testArt);
                        } else {
                            Alert.alert(MessageType.INFO, "You didn't select anything.", BXMLHandler.this);
                        }
                    }
                });
            }
        });
        
        openSheetButton7.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {
               // final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();
                fileBrowserSheet7.setRootDirectory(fileBrowserSheet6.getRootDirectory());
                fileBrowserSheet7.setMode(FileBrowserSheet.Mode.SAVE_AS);
                fileBrowserSheet7.open(BXMLHandler.this, new SheetCloseListener() {
                    @Override
                    public void sheetClosed(Sheet sheet) {
                        if (sheet.getResult()) {
                        	testHier = fileBrowserSheet7.getSelectedFile().getAbsolutePath();
                        	defaultDir = fileBrowserSheet7.getRootDirectory().getAbsolutePath();
                        	input_tehq.setText(testHier);
                        } else {
                            Alert.alert(MessageType.INFO, "You didn't select anything.", BXMLHandler.this);
                        }
                    }
                });
            }
        });
        
        openSheetButton8.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {
                //final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();
                fileBrowserSheet8.setRootDirectory(fileBrowserSheet7.getRootDirectory());
                fileBrowserSheet8.setMode(FileBrowserSheet.Mode.SAVE_AS);
                fileBrowserSheet8.open(BXMLHandler.this, new SheetCloseListener() {
                    @Override
                    public void sheetClosed(Sheet sheet) {
                        if (sheet.getResult()) {
                        	scoreData = fileBrowserSheet8.getSelectedFile().getAbsolutePath();
                        	defaultDir = fileBrowserSheet8.getRootDirectory().getAbsolutePath();
                        	input_simd.setText(scoreData);
                        } else {
                            Alert.alert(MessageType.INFO, "You didn't select anything.", BXMLHandler.this);
                        }
                    }
                });
            }
        });
        
        /*
        openSheetButton9.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {
                final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();
                fileBrowserSheet.setMode(FileBrowserSheet.Mode.SAVE_AS);
                fileBrowserSheet.open(BXMLHandler.this, new SheetCloseListener() {
                    @Override
                    public void sheetClosed(Sheet sheet) {
                        if (sheet.getResult()) {
                        	scoreData = fileBrowserSheet.getSelectedFile().getAbsolutePath();
                        	score_data.setText(scoreData);
                        } else {
                            Alert.alert(MessageType.INFO, "You didn't select anything.", BXMLHandler.this);
                        }
                    }
                });
            }
        });
        */
        
        start.getButtonPressListeners().add(new ButtonPressListener() {
			
			@Override
			public void buttonPressed(Button arg0) {
				// TODO Auto-generated method stub
				activityIndicator.setActive(true);
                setEnabled(false);
				ArrayList<SimilarityFunction> funclist = new ArrayList<SimilarityFunction>();
				if(diceCheck.isSelected())
					funclist.add(new DiceSimilarity());
				if(jacCheck.isSelected())
					funclist.add(new JaccardSimilarity());
				if(jiangCheck.isSelected())
					funclist.add(new JiangConrathSimilarity());
				if(leaCheck.isSelected())
					funclist.add(new LeacockChodorowSimilarity());
				threshold = Double.parseDouble(input_th.getText());
				data = new UIDataBinder(
						outdir, trainPara, testPara, trainArt, trainHier, 
						testArt, testHier, scoreData, funclist, threshold);
				ProcessParaTask task = new ProcessParaTask();
				TaskListener<String> taskListener = new TaskListener<String>() {
                    @Override
                    public void taskExecuted(Task<String> task) {
                        activityIndicator.setActive(false);
                        setEnabled(true);
 
                        //System.out.println("Synchronous task execution complete: \"" + task.getResult() + "\"");
                    }
 
                    @Override
                    public void executeFailed(Task<String> task) {
                        activityIndicator.setActive(false);
                        setEnabled(true);
 
                        System.err.println(task.getFault());
                    }
                };
				task.execute(new TaskAdapter<String>(taskListener));
			}
		});
    }
    
    public class ProcessParaTask extends Task<String>{

		@Override
		public String execute() throws TaskExecutionException {
			// TODO Auto-generated method stub
			ProjectWorker pw = new ProjectWorker(data);
			try {
				HashMap<String, ArrayList<ParaPairData>> scoresMap = pw.processParaPairData(pw.getPageParasMap());
				pw.saveParaSimilarityData(scoresMap, data.getTrainScoreData());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
    	
    }
}
