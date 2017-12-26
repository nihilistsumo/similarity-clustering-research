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
    String outdir, trainPara, testPara, trainArt, trainHier, testArt, testHier, scoreData;
    ArrayList<SimilarityFunction> funcs;
    double threshold;
    

    @Override
    public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
        openSheetButton1.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {
                final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();
                fileBrowserSheet.setMode(FileBrowserSheet.Mode.SAVE_TO);
                fileBrowserSheet.open(BXMLHandler.this, new SheetCloseListener() {
                    @Override
                    public void sheetClosed(Sheet sheet) {
                        if (sheet.getResult()) {
                        	//outdir = fileBrowserSheet.getRootDirectory().getAbsolutePath();
                        	outdir = fileBrowserSheet.getSelectedFile().getAbsolutePath();
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
                final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();
                fileBrowserSheet.setMode(FileBrowserSheet.Mode.SAVE_AS);
                fileBrowserSheet.open(BXMLHandler.this, new SheetCloseListener() {
                    @Override
                    public void sheetClosed(Sheet sheet) {
                        if (sheet.getResult()) {
                        	trainPara = fileBrowserSheet.getSelectedFile().getAbsolutePath();
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
                final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();
                fileBrowserSheet.setMode(FileBrowserSheet.Mode.SAVE_AS);
                fileBrowserSheet.open(BXMLHandler.this, new SheetCloseListener() {
                    @Override
                    public void sheetClosed(Sheet sheet) {
                        if (sheet.getResult()) {
                        	testPara = fileBrowserSheet.getSelectedFile().getAbsolutePath();
                        	input_tep.setText(testPara);
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
                final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();
                fileBrowserSheet.setMode(FileBrowserSheet.Mode.SAVE_AS);
                fileBrowserSheet.open(BXMLHandler.this, new SheetCloseListener() {
                    @Override
                    public void sheetClosed(Sheet sheet) {
                        if (sheet.getResult()) {
                        	trainArt = fileBrowserSheet.getSelectedFile().getAbsolutePath();
                        	input_traq.setText(trainArt);
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
                final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();
                fileBrowserSheet.setMode(FileBrowserSheet.Mode.SAVE_AS);
                fileBrowserSheet.open(BXMLHandler.this, new SheetCloseListener() {
                    @Override
                    public void sheetClosed(Sheet sheet) {
                        if (sheet.getResult()) {
                        	trainHier = fileBrowserSheet.getSelectedFile().getAbsolutePath();
                        	input_trhq.setText(trainHier);
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
                final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();
                fileBrowserSheet.setMode(FileBrowserSheet.Mode.SAVE_AS);
                fileBrowserSheet.open(BXMLHandler.this, new SheetCloseListener() {
                    @Override
                    public void sheetClosed(Sheet sheet) {
                        if (sheet.getResult()) {
                        	testArt = fileBrowserSheet.getSelectedFile().getAbsolutePath();
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
                final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();
                fileBrowserSheet.setMode(FileBrowserSheet.Mode.SAVE_AS);
                fileBrowserSheet.open(BXMLHandler.this, new SheetCloseListener() {
                    @Override
                    public void sheetClosed(Sheet sheet) {
                        if (sheet.getResult()) {
                        	testHier = fileBrowserSheet.getSelectedFile().getAbsolutePath();
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
                final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();
                fileBrowserSheet.setMode(FileBrowserSheet.Mode.SAVE_AS);
                fileBrowserSheet.open(BXMLHandler.this, new SheetCloseListener() {
                    @Override
                    public void sheetClosed(Sheet sheet) {
                        if (sheet.getResult()) {
                        	scoreData = fileBrowserSheet.getSelectedFile().getAbsolutePath();
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
				UIDataBinder data = new UIDataBinder(
						outdir, trainPara, testPara, trainArt, trainHier, 
						testArt, testHier, scoreData, funclist, threshold);
				ProjectWorker pw = new ProjectWorker(data);
				try {
					HashMap<String, ArrayList<ParaPairData>> scoresMap = pw.processParaPairData(pw.getPageParasMap());
					pw.saveParaSimilarityData(scoresMap, data.getTrainScoreData());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
    }
}
