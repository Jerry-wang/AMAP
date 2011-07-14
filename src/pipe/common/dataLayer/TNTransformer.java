/*
 * TNTransformer.java
 *
 * Created on 17 / juliol / 2007, 09:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


package pipe.common.dataLayer;

//Collections
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.OutputKeys;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * TNTransformer handles TimeNet files importation and exportation operations. 
 * It gets information from the TimeNet XML file, then passes this to DataLayer
 * constructor for construction
 *
 * @author marc
 */
public class TNTransformer {
    
    private static int inhibit = 0;
    private static int normal = 1;
    private boolean fullyCompatible;
    /** Creates a new instance of TNTransformer*/
    public TNTransformer() {
    }
    
/**
     * It exports a DataLayer to a TimeNet XML file.
     *
     * @param file     
     * @param netModel Datalayer to be exported
     */
    public void saveTN(File file, DataLayerInterface netModel) throws NullPointerException, IOException, ParserConfigurationException, DOMException, TransformerConfigurationException, TransformerException{
        saveTN(file,netModel,true);
    }
    
    /**
     * It exports a DataLayer to a TimeNet XML file.
     *
     * @param file     
     * @param netModel Datalayer to be exported
     */
    public void saveTN(File file, DataLayerInterface netModel, boolean fullyCompatible) throws NullPointerException, IOException, ParserConfigurationException, DOMException, TransformerConfigurationException, TransformerException{
                this.fullyCompatible=fullyCompatible;
                Document pnDOM = null;
		int i;
		StreamSource xsltSource = null;
		Transformer transformer = null;
		try {
			// Build a Petri Net XML Document
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			pnDOM = builder.newDocument();

			Element TN = pnDOM.createElement("net"); //TN root element
			pnDOM.appendChild(TN);

			Attr ns = pnDOM.createAttribute("xmlns"); // TN "xmlns" Attribute
			ns.setValue("http://pdv.cs.tu-berlin.de/TimeNET/schema/eDSPN");
			TN.setAttributeNode(ns);
                        
                        Attr TNid = pnDOM.createAttribute("id");
                        TNid.setValue("0");
                        TN.setAttributeNode(TNid);
                        
                        Attr TNNetClass = pnDOM.createAttribute("netclass");
                        TNNetClass.setValue("eDSPN");
                        TN.setAttributeNode(TNNetClass);
                        
                        TN.setAttribute("xmlns","http://pdv.cs.tu-berlin.de/TimeNET/schema/eDSPN");
                        TN.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
                        TN.setAttribute("xsi:schemaLocation","http://pdv.cs.tu-berlin.de/TimeNET/schema/eDSPN etc/schemas/eDSPN.xsd");
                        
			Place[] places = netModel.getPlaces();
			for(i = 0 ; i < places.length ; i++) {
				TN.appendChild(createTNPlaceElement(places[i], pnDOM));
			}
			places = null;

                        /*
			AnnotationNote[] labels = netModel.getLabels();
			if (labels.length>0) {
				for (i = 0; i <labels.length; i++) {
					NET.appendChild(createAnnotationNoteElement(labels[i], pnDOM));
				}
				labels = null;
			}
                        */
                        
			Transition[] transitions = netModel.getTransitions();
                        for(i = 0 ; i < transitions.length ; i++){
                            if(transitions[i].isTimed()){
                                    TN.appendChild(this.createTNExponentialTransitionElement(transitions[i],pnDOM));
                                }
                        }
			for(i = 0 ; i < transitions.length ; i++) {
                                if(!transitions[i].isTimed()){
                                    TN.appendChild(this.createTNImmediateTransitionElement(transitions[i],pnDOM));
                                }
			}
			transitions = null;
                        
			Arc[] arcs = netModel.getArcs();
			for(i = 0 ; i < arcs.length ; i++) {
				Element newArc = createTNArcElement(arcs[i],pnDOM,normal);

				/*int arcPoints = arcs[i].getArcPath().getArcPathDetails().length;
				String[][] point = arcs[i].getArcPath().getArcPathDetails();
				for (int j = 0; j < arcPoints; j++){
					//newArc.appendChild(createArcPoint(point[j][0],point[j][1],point[j][2],pnDOM,j));
				}*/
				TN.appendChild(newArc);
				//newArc = null;
			}
			arcs = null;
                        
                        InhibitorArc [] inhibitorArcs = netModel.getInhibitors();
                        for (i = 0; i < inhibitorArcs.length; i++) {
                            Element newArc = createTNArcElement(inhibitorArcs[i],pnDOM,inhibit);
                            TN.appendChild(newArc);
                        }
                        inhibitorArcs=null;

			pnDOM.normalize();
		
                        
                        File fitxer = new File(file.getPath());
                        StreamResult resultat = new StreamResult(fitxer);
                        Transformer xformer = TransformerFactory.newInstance().newTransformer();

			DOMSource source = new DOMSource(pnDOM);
                        
                        xformer.setOutputProperty(OutputKeys.INDENT, "yes");
                        xformer.transform(source, resultat);

		} catch (ParserConfigurationException e) {
//			System.out.println("=====================================================================================");
			System.out.println("ParserConfigurationException thrown in savePNML() : dataLayerWriter Class : dataLayer Package: filename=\"" + file.getCanonicalPath() + "\" xslt=\"" + xsltSource.getSystemId() + "\" transformer=\"" + transformer.getURIResolver() + "\"");
//			System.out.println("=====================================================================================");
//			e.printStackTrace(System.err);
		} catch (DOMException e) {
//			System.out.println("=====================================================================");
			System.out.println("DOMException thrown in savePNML() : dataLayerWriter Class : dataLayer Package: filename=\"" + file.getCanonicalPath() + "\" xslt=\"" + xsltSource.getSystemId() + "\" transformer=\"" + transformer.getURIResolver() + "\"");
//			System.out.println("=====================================================================");
//			e.printStackTrace(System.err);
		} catch (TransformerConfigurationException e) {
//			System.out.println("==========================================================================================");
			System.out.println("TransformerConfigurationException thrown in savePNML() : dataLayerWriter Class : dataLayer Package: filename=\"" + file.getCanonicalPath() + "\" xslt=\"" + xsltSource.getSystemId() + "\" transformer=\"" + transformer.getURIResolver() + "\"");
//			System.out.println("==========================================================================================");
//			e.printStackTrace(System.err);
		} catch (TransformerException e) {
//			System.out.println("=============================================================================");
			System.out.println("TransformerException thrown in savePNML() : dataLayerWriter Class : dataLayer Package: filename=\"" + file.getCanonicalPath() + "\" xslt=\"" + xsltSource.getSystemId() + "\" transformer=\"" + transformer.getURIResolver() + "\"" + e);
//			System.out.println("=============================================================================");
//			e.printStackTrace(System.err);
		}

        }
        
        private Element createTNPlaceElement(Place inputPlace, Document document){

		Element placeElement = null;

		if(document != null) {
			placeElement = document.createElement("place");
		}

		if(inputPlace != null ) {
			Integer attrValue = null;
                        String idInput;
                        String nameInput;
                        if (fullyCompatible){
                            idInput = inputPlace.getId().replaceAll(" ","");
                            nameInput = inputPlace.getName().replaceAll(" ","");
                        }else{
                            idInput = inputPlace.getId();
                            nameInput = inputPlace.getName();
                        }
//			Double nameOffsetYInput = inputPlace.getNameOffsetXObject();
//			Double nameOffsetXInput = inputPlace.getNameOffsetXObject();
            LinkedList<Marking> initialMarkingInput = (LinkedList<Marking>)ObjectDeepCopier.deepCopy(inputPlace.getCurrentMarkingObject());
			/*Double markingOffsetXInput = inputPlace.getMarkingOffsetXObject();
			Double markingOffsetYInput = inputPlace.getMarkingOffsetYObject();*/

			placeElement.setAttribute("id", (idInput != null ? idInput : (nameInput != null && nameInput.length() > 0? nameInput : "")));
			//placeElement.setAttribute("id", (idInput != null ? idInput : "error"));
//			placeElement.setAttribute("nameOffsetX", (nameOffsetXInput != null ? String.valueOf(nameOffsetXInput) : ""));
//			placeElement.setAttribute("nameOffsetY", (nameOffsetYInput != null ? String.valueOf(nameOffsetYInput) : ""));
			placeElement.setAttribute("initialMarking", (initialMarkingInput != null ? String.valueOf(initialMarkingInput) : "0"));
			//placeElement.setAttribute("markingOffsetX", (markingOffsetXInput != null ? String.valueOf(markingOffsetXInput) : ""));
			//placeElement.setAttribute("markingOffsetY", (markingOffsetYInput != null ? String.valueOf(markingOffsetYInput) : ""));
                        if (inputPlace.getCapacity()!=0){
                            placeElement.setAttribute("capacity", String.valueOf(inputPlace.getCapacity()));
                        }
                        placeElement.setAttribute("type","node");
                        
                        Double x = inputPlace.getPositionXObject();
                        Double y = inputPlace.getPositionYObject();
                        
                        Element placeGraphics = createTNGraphics(0,x.intValue(),y.intValue(),document); 
                        placeGraphics.setAttribute("orientation","0"); //de moment
                                                
			placeElement.appendChild(placeGraphics);
                        
                        Element placeLabel = createTNLabel(nameInput,"L"+nameInput,0,0,document);
                        placeElement.appendChild(placeLabel);
                        

		}
		return placeElement;
	}
        
        private Element createTNImmediateTransitionElement(Transition inputTransition, Document document){
		Element transitionElement = null;

		if(document != null) {
			transitionElement = document.createElement("immediateTransition");
		}

		if(inputTransition != null ) {
			Integer attrValue = null;
			Double positionXInput = inputTransition.getPositionXObject();
			Double positionYInput = inputTransition.getPositionYObject();
                        String idInput;
                        String nameInput;
                        if (fullyCompatible){
                            idInput = inputTransition.getId().replaceAll(" ","");
                            nameInput = inputTransition.getName().replaceAll(" ","");
                        }else{
                            idInput = inputTransition.getId();
                            nameInput = inputTransition.getName();
                        }
			double aRate = inputTransition.getRate();
                        int priority = (int) inputTransition.getPriority();

                        transitionElement.setAttribute("weight", (aRate != 1 ? String.valueOf(aRate):"1"));
                        transitionElement.setAttribute("priority", (priority != 1 ? String.valueOf(priority):"1"));
                        Element transLabel = createTNLabel(nameInput,"L"+nameInput,0,0,document);
                        Element transGraph = this.createTNGraphics(0,positionXInput.intValue(),
                                positionYInput.intValue(),document);
                        transitionElement.appendChild(transGraph);
                        transitionElement.appendChild(transLabel);
			transitionElement.setAttribute("type", "node");
			transitionElement.setAttribute("id", (idInput != null ? idInput : "error"));
		}

		return transitionElement;
	}
        private Element createTNExponentialTransitionElement(Transition inputTransition, Document document){
		Element transitionElement = null;

		if(document != null) {
			transitionElement = document.createElement("exponentialTransition");
		}

		if(inputTransition != null ) {
			Integer attrValue = null;
			Double positionXInput = inputTransition.getPositionXObject();
			Double positionYInput = inputTransition.getPositionYObject();
                        String idInput;
                        String nameInput;
                        if(fullyCompatible){
                            idInput = inputTransition.getId().replaceAll(" ","");
                            nameInput = inputTransition.getName().replaceAll(" ","");
                        }else{
                            idInput = inputTransition.getId();
                            nameInput = inputTransition.getName();
                        }
			double aRate = inputTransition.getRate();
                        boolean infinite = inputTransition.isInfiniteServer();

                        Element transLabel = createTNLabel(nameInput,"L"+nameInput,0,0,document);
                        Element transGraph = this.createTNGraphics(0,positionXInput.intValue(),
                                positionYInput.intValue(),document);
                        transitionElement.appendChild(transGraph);
                        transitionElement.appendChild(transLabel);
                        transitionElement.setAttribute("delay", ""+1.0/aRate);
                        if (infinite){
                            transitionElement.setAttribute("serverType", "InfiniteServer");
                        }else{
                            transitionElement.setAttribute("serverType", "ExclusiveServer");
                        }
			transitionElement.setAttribute("type", "node");
			transitionElement.setAttribute("id", (idInput != null ? idInput : "error"));
		}

		return transitionElement;
	}
        
        private Element createTNArcElement(Arc inputArc, Document document, int type){
		Element arcElement = null;

		if(document != null) {
                        if(type == inhibit)
                            arcElement = document.createElement("inhibit");
                        else
                            arcElement = document.createElement("arc");
		}   

		if(inputArc != null ) {
			//Double positionXInputD = (int)inputArc.getStartPositionX();
			//Double positionXInput = new Double (positionXInputD);
			//double positionYInputD = (int)inputArc.getStartPositionY();
			//Double positionYInput = new Double (positionXInputD);
                        String idInput;
                        String sourceInput;
                        String targetInput;
                        if(fullyCompatible){
                            idInput = inputArc.getId().replaceAll(" ","");
                            sourceInput = inputArc.getSource().getId().replaceAll(" ","");
                            targetInput = inputArc.getTarget().getId().replaceAll(" ","");
                        }else{
                            idInput = inputArc.getId();
                            sourceInput = inputArc.getSource().getId();
                            targetInput = inputArc.getTarget().getId();
                        }
		//	int inscriptionInput = (inputArc != null ? inputArc.getWeight() : 1);
//			Double inscriptionPositionXInput = inputArc.getInscriptionOffsetXObject();
//			Double inscriptionPositionYInput = inputArc.getInscriptionOffsetYObject();
			
//			arcElement.setAttribute("inscriptionOffsetX", (inscriptionPositionXInput != null ? String.valueOf(inscriptionPositionXInput) : ""));
//			arcElement.setAttribute("inscriptionOffsetY", (inscriptionPositionYInput != null ? String.valueOf(inscriptionPositionYInput) : ""));
                        
                        arcElement.setAttribute("id", (idInput != null ? idInput : "error"));
                        arcElement.setAttribute("fromNode",(sourceInput != null ? sourceInput : ""));
                        arcElement.setAttribute("toNode",(targetInput != null ? targetInput : ""));
                        arcElement.setAttribute("type","connector");
                        String inscriptionText="1";
                       // if (inputArc.getWeight()!=1){
                            arcElement.setAttribute("weight",String.valueOf(inputArc.getWeight()));
                            inscriptionText=String.valueOf(inputArc.getWeight());
                     //   }
                        Element inscription = this.createTNInscription(inscriptionText,"I"+idInput,document);
                        arcElement.appendChild(inscription);


		}
		return arcElement;
	}
        
        
        private Element createTNInscription(String text, String id, Document document){
            Element inscription = document.createElement("inscription");
            inscription.setAttribute("type","inscriptionText");
            inscription.setAttribute("id",id);
            inscription.setAttribute("text",text);
            
            Element graphics = createTNGraphics(null,0,0,document);//canviar 0,0 per inscrPosX i Y
            inscription.appendChild(graphics);
            
            return inscription;
        }
        private Element createTNGraphics(Integer orientation, Integer x, Integer y, Document document){
                Element graphics = document.createElement("graphics");
                if(orientation != null){
                    graphics.setAttribute("orientation",Integer.toString(orientation)); //de moment
                }
                graphics.setAttribute("x",Integer.toString(x.intValue()));
                graphics.setAttribute("y",Integer.toString(y.intValue()));
                return graphics;
        }
        private Element createTNLabel(String text, String id, Integer x, Integer y, Document document){
            Element label = document.createElement("label");
            label.setAttribute("text",text);
            label.setAttribute("type","text");
            label.setAttribute("id",id);
            Element labelGraphics = createTNGraphics(0,x,y,document);
            label.appendChild(labelGraphics);
            return label;
        }
        
        /**
	 * Return a DOM for the TimeNet file at URI filename
	 *
	 * @param fileName TN file URI
	 * @return A DOM for the TN file
	 */
        
        public Document transformTN(String filename){
            return transformTN(new File(filename));
        }
        public Document transformTN(File file)
	//	    System.out.println("========================================================");
//	System.out.println("dataLayer Loading filename=\"" + filename + "\"");
//	System.out.println("========================================================");
	{
                Document document = null;
		File outputObjectArrayList=null;
		//Document document = null;
                DocumentBuilderFactory factory =  DocumentBuilderFactory.newInstance();
                // then we have to create document-loader:
                try{
                    DocumentBuilder loader = factory.newDocumentBuilder();
                    // loading a DOM-tree...
                    document = loader.parse(file);
          
                    StreamSource xsltSource = new StreamSource(
					Thread.currentThread().getContextClassLoader().getResourceAsStream(
							"xslt" + System.getProperty("file.separator")
							+ "TNtoPipe.xsl"));//new StreamSource("/home/marc/pfc/transformar/TNtoPipe.xsl");

                    // Write the DOM document to the file
                    Transformer xformer = TransformerFactory.newInstance().newTransformer(xsltSource);
                    DOMSource source = new DOMSource(document);
                    

                    // TRY TO DO ALL IN MEMORT TO REDUCE READ-WRITE DELAYS
                    outputObjectArrayList = new File(System.getProperty("java.io.tmpdir")+System.getProperty("file.separator")+"ObjectList.xml"); // Output for XSLT Transformation
                    outputObjectArrayList.deleteOnExit();
                    StreamResult result = new StreamResult(outputObjectArrayList);
                    xformer.transform(source, result);
                            
			// Get DOM for transformed document
			document = getDOM(outputObjectArrayList);
		}

		catch (IOException e) {
			System.out.println("IOException thrown in loadPNML(String filename) : dataLayer Class : dataLayer Package");
			e.printStackTrace(System.err);
		} catch (org.xml.sax.SAXException e) {
			System.out.println("SAXException thrown in loadPNML(String filename) : dataLayer Class : dataLayer Package");
			e.printStackTrace(System.err);
		} catch (TransformerException e) {
			System.out.println("TransformerException thrown in loadPNML(String filename) : dataLayer Class : dataLayer Package");
			e.printStackTrace(System.err);
		} catch (ParserConfigurationException e) {
			System.out.println("ParserConfigurationException thrown in loadPNML(String filename) : dataLayer Class : dataLayer Package");
			e.printStackTrace(System.err);
		}

//		Delete transformed file
		if(outputObjectArrayList!=null) outputObjectArrayList.delete();

		return document;
		//BK - surely I want to make everything exact?? - ignore below in favour of catches above
		//	 Maxim - make it throw out any exception it gets to the caller. Debugging message left in for now.
		//} catch (Exception e) {
		//  throw new RuntimeException(e);
		//}	    
	}
        
        
        /**
	 * Return a DOM for the PNML File pnmlFile
	 *
	 * @param pnmlFile File Object for PNML of Petri-Net
	 * @return A DOM for the File Object for PNML of Petri-Net
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public Document getDOM(File timeNetFile)
	throws ParserConfigurationException, IOException{

		Document document = null;

		try {

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setIgnoringElementContentWhitespace(true);
			// POSSIBLY ADD VALIDATING
			// documentBuilderFactory.setValidating(true);
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.parse(timeNetFile);

		}catch (org.xml.sax.SAXException e) {
			System.err.println("org.xml.sax.SAXException thrown in getDom(String pnmlFileName) : dataLayer Class : dataLayer Package" + e);
			System.err.println("Workaround: delete the xmlns attribute from the PNML root node.  Probably not ideal, to be fixed when time allows.");
                }
                catch (ParserConfigurationException e) {
			System.err.println("javax.xml.parsers.ParserConfigurationException thrown in getDom(String pnmlFileName) : dataLayer Class : dataLayer Package");
		}
		catch (IOException e) {
			System.err.println("ERROR: File may not be present or have the correct attributes");
			System.err.println("java.io.IOException thrown in getDom(String pnmlFileName) : dataLayer Class : dataLayer Package" + e);
		}

		return document;
	}
}
