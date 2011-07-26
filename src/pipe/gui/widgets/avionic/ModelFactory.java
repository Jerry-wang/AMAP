package pipe.gui.widgets.avionic;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import pipe.gui.widgets.avionic.GuideVO.RTInfo;
/**
 * 根据给定的GuideVO，来生成一个xml文件，是对应的模型，打算用JDOM
 * @author Jerry-wang   这个为了赶时间 写的超级丑陋  下次重构下吧
 *
 */
public class ModelFactory {
	
	public static String buildModel(GuideVO vo)
	{
		if(vo.getTypeOfBus()==GuideVO.THE_AFDX)
			return buildAfdxModel(vo);
		else
			return "not finished";
	}
	
	private static String buildAfdxModel(GuideVO vo)
	{
		if(vo.getWayOfSchedule()==GuideVO.PRIORITY)
			return buildAfdxPriorityModel(vo);
		else if(vo.getWayOfSchedule()==GuideVO.FIFO)
			return "fifo not finished";
		else if(vo.getWayOfSchedule()==GuideVO.ONE_BY_ONE)
			return "one_by_one not finished";
		else
			return "no this way of schedule";
		
	}
	private static String buildAfdxPriorityModel(GuideVO vo)
	{
		//LinkedList<TokenClass> tokenClasses = CreateGui.getModel().getTokenClasses();;
		Element  pnml, net, tokenclass, place, transition, arc; 
		   
		pnml = new Element("pnml");
		net = new Element("net"); 
		tokenclass = new Element("tokenclass");
		place = new Element("place");
		transition = new Element("transition"); 
		arc = new Element("arc"); 
		
		
		net.setAttribute("id", "Net-One");
		net.setAttribute("type", "P/T net");
		
		Element tokenClass = new Element("tokenClass");
		tokenClass.setAttribute("id","Black");
		tokenClass.setAttribute("enabled","true");
		tokenClass.setAttribute("red","0");
		tokenClass.setAttribute("green","0");
		tokenClass.setAttribute("blue","0");
		net.addContent(tokenClass);
		
		tokenClass = new Element("tokenClass");
		tokenClass.setAttribute("id","Red");
		tokenClass.setAttribute("enabled","true");
		tokenClass.setAttribute("red","255");
		tokenClass.setAttribute("green","0");
		tokenClass.setAttribute("blue","0");
		net.addContent(tokenClass);
		
		tokenClass = new Element("tokenClass");
		tokenClass.setAttribute("id","Green");
		tokenClass.setAttribute("enabled","true");
		tokenClass.setAttribute("red","0");
		tokenClass.setAttribute("green","255");
		tokenClass.setAttribute("blue","0");
		net.addContent(tokenClass);
		
 		
		addRTs(vo, net);
		addSchedule(vo, net);
 		pnml.addContent(net);
		Document doc = new Document(pnml);
		Format format = Format.getCompactFormat();
        format.setEncoding("iso-8859-1");           //设置xml文件的字符为iso-8859-1
//		format.setEncoding("GB2312");
        format.setIndent("    ");               //设置xml文件的缩进为4个空格
//        String path = System.getProperty("user.dir")+"//bin//AFDX_3_PRIORITY.xml";
        
        String path = "c://AFDX_3_PRIORITY.xml";
        XMLOutputter XMLOut = new XMLOutputter(format);//在元素后换行，每一层元素缩排四格
        try {
        	
//			XMLOut.output(doc, new FileOutputStream("C://Users//Administrator//Desktop//test1234.xml"));
        	
        	XMLOut.output(doc, new FileOutputStream(path));
		} catch (FileNotFoundException e) {
 			e.printStackTrace();
		} catch (IOException e) {
 			e.printStackTrace();
		}
		
		//返回的是生成模型的路径
		return path;
	}
	
	private static void addArc(Element source, Element target,  Element net , int times , boolean isInhibitor) {
		 //是上面的数字 一般是1  默认是1
			Element arc1 = new Element("arc");
	 		arc1.setAttribute("id", ""+source.getAttributeValue("id")+" to "+target.getAttributeValue("id"));
	 		arc1.setAttribute("source",source.getAttributeValue("id"));
	 		arc1.setAttribute("target",target.getAttributeValue("id"));
	 		Element graphics = new Element("graphics");
	 		arc1.addContent(graphics);
	 		
	 		Element inscription = new Element("inscription");
	 		Element value = new Element("value");
	 		
	 		String str ="Black,"+times;
	 		value.setText(str);
	 		inscription.addContent(value);
	 		graphics = new Element("graphics");
	 		inscription.addContent(graphics);
	 		arc1.addContent(inscription);
	 		
	 		Element tagged = new Element("tagged");
	 		value = new Element("value");
	 		str ="false";
	 		value.setText(str);
	 		tagged.addContent(value);
	 		arc1.addContent(tagged);
	 		
	 		Element arcpath = new Element("arcpath");
	 		arcpath.setAttribute("id", "000");
	 		arcpath.setAttribute("x", "000");
	 		arcpath.setAttribute("y", "000");
	 		arcpath.setAttribute("curvePoint", "false");


	  		arc1.addContent(arcpath);
	 		
	  		arcpath = new Element("arcpath");
	 		arcpath.setAttribute("id", "001");
	 		arcpath.setAttribute("x", "000");
	 		arcpath.setAttribute("y", "000");
	 		arcpath.setAttribute("curvePoint", "false");
	 		arc1.addContent(arcpath);
	 		
	 		Element type = new Element("type");
	 		if(isInhibitor==false)
	 			type.setAttribute("value", "normal");
	 		else
	 			type.setAttribute("value", "inhibitor");
	  		arc1.addContent(type);
	  		net.addContent(arc1); 
		 
	}
	
	private static void addSchedule(GuideVO vo, Element net) {
		Element placeOfmiddle = new Element("place");
 		placeOfmiddle.setAttribute("id", "P"+pIndex);
 		
		for(int i=0; i<vo.getNumOfRTs(); i++)
		{
			Element transition1 = new Element("transition");
	 		transition1.setAttribute("id", "T"+tIndex);
	 		Element graphics = new Element("graphics");
	 		Element transition1Position = new Element("position");
	 		
	 		transition1Position.setAttribute("x", String.valueOf(650));//位置问题
	 		transition1Position.setAttribute("y", String.valueOf(100+i*150));
	 		graphics.addContent(transition1Position);
	 		transition1.addContent(graphics);
	 		Element name = new Element("name");
	 		Element value = new Element("value");
	 		
	 		value.setText("T"+tIndex++);
	 		Element offset = new Element("offset");
	 		
	 		offset.setAttribute("x", "-5.0");
	 		offset.setAttribute("y", "35.0");
	 		
	 		graphics = new Element("graphics");
	 		graphics.addContent(offset);
	 		
	 		name.addContent(value);
	 		name.addContent(graphics);
	 		transition1.addContent(name);
	 		
	 		Element orentation = new Element("orentation");
	 		value = new Element("value");
	 		String str ="0";
	 		value.setText(str);
	 		orentation.addContent(value);
	 		transition1.addContent(orentation);
	 		
	 		Element rate = new Element("rate");
	 		value = new Element("value");
	 		str ="0";
	 		value.setText(str);
	 		rate.addContent(value);
	 		transition1.addContent(rate);
	 		
	 		Element timed = new Element("timed");
	 		value = new Element("value");
	 		str ="false";
	 		value.setText(str);
	 		timed.addContent(value);
	 		transition1.addContent(timed);
	 		
	 		Element deterministic = new Element("deterministic");
	 		value = new Element("value");
	 		str ="false";
	 		value.setText(str);
	 		deterministic.addContent(value);
	 		transition1.addContent(deterministic);
	 		
	 		Element infiniteServer = new Element("infiniteServer");
	 		value = new Element("value");
	 		str ="false";
	 		value.setText(str);
	 		infiniteServer.addContent(value);
	 		transition1.addContent(infiniteServer);
	 		
	 		Element priorty = new Element("priorty");
	 		value = new Element("value");
	 		str ="0"; 
	 		value.setText(str);
	 		priorty.addContent(value);
	 		transition1.addContent(priorty);
	 		
	 		Element delay = new Element("delay"); 
	 		value = new Element("value");
	 		str ="0";  
	 		value.setText(str);
	 		delay.addContent(value); 
	 		transition1.addContent(delay); 
	 		 
	 		net.addContent(transition1);
	 		
	 		listOfScheduleStartTransition.add(transition1);
	  
	 		Element arc1 = new Element("arc");
	 		arc1.setAttribute("id", ""+transition1.getAttributeValue("id")+" to "+placeOfmiddle.getAttributeValue("id"));
	 		arc1.setAttribute("source",transition1.getAttributeValue("id"));
	 		arc1.setAttribute("target",placeOfmiddle.getAttributeValue("id"));
	 		graphics = new Element("graphics");
	 		arc1.addContent(graphics);
	 		
	 		Element inscription = new Element("inscription");
	 		value = new Element("value");
	 		str ="Black,1";
	 		value.setText(str);
	 		inscription.addContent(value);
	 		graphics = new Element("graphics");
	 		inscription.addContent(graphics);
	 		arc1.addContent(inscription);
	 		
	 		Element tagged = new Element("tagged");
	 		value = new Element("value");
	 		str ="false";
	 		value.setText(str);
	 		tagged.addContent(value);
	 		arc1.addContent(tagged);
	 		
	 		Element arcpath = new Element("arcpath");
	 		arcpath.setAttribute("id", "000");
	 		arcpath.setAttribute("x", "000");
	 		arcpath.setAttribute("y", "000");
	 		arcpath.setAttribute("curvePoint", "false");


	  		arc1.addContent(arcpath);
	 		
	  		arcpath = new Element("arcpath");
	 		arcpath.setAttribute("id", "001");
	 		arcpath.setAttribute("x", "000");
	 		arcpath.setAttribute("y", "000");
	 		arcpath.setAttribute("curvePoint", "false");
	 		arc1.addContent(arcpath);
	 		
	 		Element type = new Element("type");
	 		type.setAttribute("value", "normal");
	  		arc1.addContent(type);
	  		net.addContent(arc1); 
	  		
	  		if(i==vo.getNumOfRTs()/2)
	 		{
	 			//placeOfmiddle = new Element("place");
	 		//placeOfmiddle.setAttribute("id", "P"+pIndex);
	 	 		graphics = new Element("graphics");
	 	 		Element placePosition = new Element("position");
	 	 		
	 	 		placePosition.setAttribute("x", String.valueOf(850));
	 	 		placePosition.setAttribute("y", String.valueOf(100+i*150));
	 	 		graphics.addContent(placePosition);
	 	 		placeOfmiddle.addContent(graphics);
	 	 		
	 	 		name = new Element("name");
	 	 		value = new Element("value");
	 	 		
	 	 		value.setText("P"+pIndex++);
	 	 		offset = new Element("offset");
	 	 		
	 	 		offset.setAttribute("x", "-5.0");
	 	 		offset.setAttribute("y", "35.0");
	 	 		
	 	 		graphics = new Element("graphics");
	 	 		graphics.addContent(offset);
	 	 		
	 	 		name.addContent(value);
	 	 		name.addContent(graphics);
	 	 		placeOfmiddle.addContent(name);
	 	 		
	 	 		Element initialMarking = new Element("initialMarking");
	 	 		value = new Element("value");
	 	  		str = "Black,0";
	 	  		 
	 	 		value.setText(str);
	 	 		
	 	 		graphics = new Element("graphics");
	 	 		offset = new Element("offset");
	 	 		offset.setAttribute("x", "0");
	 	 		offset.setAttribute("y", "0");
	 	 		graphics.addContent(offset);
	 	 		initialMarking.addContent(value);
	 	 		initialMarking.addContent(graphics);
	 	 		placeOfmiddle.addContent(initialMarking);
	 	 		
	 	 		Element capacity = new Element("capacity");
	 	 		value = new Element("value");
	 	 		value.setText("0");
	 	 		capacity.addContent(value);
	 	 		
	 	 		placeOfmiddle.addContent(capacity);
	 	 		
	 	 		net.addContent(placeOfmiddle);
	 	 		
	 	 		Element transition2 = new Element("transition");
	 	 		transition2.setAttribute("id", "T"+tIndex);
	 	 		Element graphics2 = new Element("graphics");
	 	 		Element transition2Position = new Element("position");
	 	 		
	 	 		transition2Position.setAttribute("x", String.valueOf(950));//位置问题
	 	 		transition2Position.setAttribute("y", String.valueOf(100+i*150));
	 	 		graphics2.addContent(transition2Position);
	 	 		transition2.addContent(graphics2);
	 	 		Element name2 = new Element("name");
	 	 		Element value2 = new Element("value");
	 	 		
	 	 		value2.setText("T"+tIndex++);
	 	 		Element offset2 = new Element("offset");
	 	 		
	 	 		offset2.setAttribute("x", "-5.0");
	 	 		offset2.setAttribute("y", "35.0");
	 	 		
	 	 		graphics2 = new Element("graphics");
	 	 		graphics2.addContent(offset2);
	 	 		
	 	 		name2.addContent(value2);
	 	 		name2.addContent(graphics2);
	 	 		transition1.addContent(name2);
	 	 		
	 	 		Element orentation2 = new Element("orentation");
	 	 		value2 = new Element("value");
	 	 		String str2 ="0";
	 	 		value2.setText(str2);
	 	 		orentation2.addContent(value2);
	 	 		transition2.addContent(orentation2);
	 	 		
	 	 		Element rate2 = new Element("rate");
	 	 		value2 = new Element("value");
	 	 		str2 ="0";
	 	 		value2.setText(str2);
	 	 		rate2.addContent(value2);
	 	 		transition2.addContent(rate2);
	 	 		
	 	 		Element timed2 = new Element("timed");
	 	 		value2 = new Element("value");
	 	 		str2 ="false";
	 	 		value2.setText(str);
	 	 		timed2.addContent(value2);
	 	 		transition2.addContent(timed2);
	 	 		
	 	 		Element deterministic2 = new Element("deterministic");
	 	 		value2 = new Element("value");
	 	 		str2 ="false";
	 	 		value2.setText(str2);
	 	 		deterministic2.addContent(value2);
	 	 		transition2.addContent(deterministic2);
	 	 		
	 	 		Element infiniteServer2 = new Element("infiniteServer");
	 	 		value2 = new Element("value");
	 	 		str2 ="false";
	 	 		value2.setText(str2);
	 	 		infiniteServer2.addContent(value2);
	 	 		transition2.addContent(infiniteServer2);
	 	 		
	 	 		Element priorty2 = new Element("priorty");
	 	 		value2 = new Element("value");
	 	 		str2 ="0"; 
	 	 		value2.setText(str2);
	 	 		priorty2.addContent(value2);
	 	 		transition2.addContent(priorty2);
	 	 		
	 	 		Element delay2 = new Element("delay"); 
	 	 		value2 = new Element("value");
	 	 		str2 ="0";  
	 	 		value2.setText(str2);
	 	 		delay2.addContent(value2); 
	 	 		transition2.addContent(delay2); 
	 	 		 System.out.println("transition2222222222222");
	 	 		net.addContent(transition2);
	 	 		
	 	 		addArc(placeOfmiddle,transition2,net,1, false);
	 	 		
	 	 		Element place3 = new Element("place");
	 	 		place3.setAttribute("id", "P"+pIndex);
	 	 		graphics = new Element("graphics");
	 	 		Element placePosition3 = new Element("position");
	 	 		
	 	 		placePosition3.setAttribute("x", String.valueOf(1050));
	 	 		placePosition3.setAttribute("y", String.valueOf(100+i*150));
	 	 		graphics.addContent(placePosition3);
	 	 		place3.addContent(graphics);
	 	 		
	 	 		name = new Element("name");
	 	 		value = new Element("value");
	 	 		
	 	 		value.setText("P"+pIndex++);
	 	 		offset = new Element("offset");
	 	 		
	 	 		offset.setAttribute("x", "-5.0");
	 	 		offset.setAttribute("y", "35.0");
	 	 		
	 	 		graphics = new Element("graphics");
	 	 		graphics.addContent(offset);
	 	 		
	 	 		name.addContent(value);
	 	 		name.addContent(graphics);
	 	 		place3.addContent(name);
	 	 		
	 	 		 initialMarking = new Element("initialMarking");
	 	 		value = new Element("value");
	 	  		str = "Black,0";
	 	  		 
	 	 		value.setText(str);
	 	 		
	 	 		graphics = new Element("graphics");
	 	 		offset = new Element("offset");
	 	 		offset.setAttribute("x", "0");
	 	 		offset.setAttribute("y", "0");
	 	 		graphics.addContent(offset);
	 	 		initialMarking.addContent(value);
	 	 		initialMarking.addContent(graphics);
	 	 		place3.addContent(initialMarking);
	 	 		
	 	 		 capacity = new Element("capacity");
	 	 		value = new Element("value");
	 	 		value.setText("0");
	 	 		capacity.addContent(value);
	 	 		
	 	 		place3.addContent(capacity);
	 	 		
	 	 		net.addContent(place3);
	 	 		
	 	 		addArc(transition2,place3,  net,1 ,false); 
	 	 		
	 	 		
	 	 		Element transition4 = new Element("transition");
	 	 		transition4.setAttribute("id", "T"+tIndex);
		 		Element graphics4 = new Element("graphics");
		 		Element transition1Position4 = new Element("position");
		 		
		 		transition1Position4.setAttribute("x", String.valueOf(1200));//位置问题
		 		transition1Position4.setAttribute("y", String.valueOf(100+i*150));
		 		graphics4.addContent(transition1Position4);
		 		transition4.addContent(graphics4);
		 		name = new Element("name");
		 		value = new Element("value");
		 		
		 		value.setText("T"+tIndex++);
		 		offset = new Element("offset");
		 		
		 		offset.setAttribute("x", "-5.0");
		 		offset.setAttribute("y", "35.0");
		 		
		 		graphics = new Element("graphics");
		 		graphics.addContent(offset);
		 		
		 		name.addContent(value);
		 		name.addContent(graphics);
		 		transition4.addContent(name);
		 		
		 		orentation = new Element("orentation");
		 		value = new Element("value");
		 		str ="0";
		 		value.setText(str);
		 		orentation.addContent(value);
		 		transition4.addContent(orentation);
		 		
		 		rate = new Element("rate");
		 		value = new Element("value");
		 		str ="0";
		 		value.setText(str);
		 		rate.addContent(value);
		 		transition4.addContent(rate);
		 		
		 		timed = new Element("timed");
		 		value = new Element("value");
		 		str ="false";
		 		value.setText(str);
		 		timed.addContent(value);
		 		transition4.addContent(timed);
		 		
		 		deterministic = new Element("deterministic");
		 		value = new Element("value");
		 		str ="true";
		 		value.setText(str);
		 		deterministic.addContent(value);
		 		transition4.addContent(deterministic);
		 		
		 		infiniteServer = new Element("infiniteServer");
		 		value = new Element("value");
		 		str ="false";
		 		value.setText(str);
		 		infiniteServer.addContent(value);
		 		transition4.addContent(infiniteServer);
		 		
		 		priorty = new Element("priorty");
		 		value = new Element("value");
		 		str ="0"; 
		 		value.setText(str);
		 		priorty.addContent(value);
		 		transition4.addContent(priorty);
		 		
		 		delay = new Element("delay"); 
		 		value = new Element("value");
		 		str ="0";  
		 		value.setText(str);
		 		delay.addContent(value); 
		 		transition4.addContent(delay); 
		 		 
		 		net.addContent(transition4);
	 	 		
		 		addArc(place3, transition4,  net, 1, false);
	 	 		
	 	 		
	 	 		Element place4 = new Element("place");
	 	 		place4.setAttribute("id", "P"+pIndex);
	 	 		graphics = new Element("graphics");
	 	 		Element placePosition4 = new Element("position");
	 	 		
	 	 		placePosition4.setAttribute("x", String.valueOf(1400));
	 	 		placePosition4.setAttribute("y", String.valueOf(100+i*150));
	 	 		graphics.addContent(placePosition4);
	 	 		place4.addContent(graphics);
	 	 		
	 	 		name = new Element("name");
	 	 		value = new Element("value");
	 	 		
	 	 		value.setText("P"+pIndex++);
	 	 		offset = new Element("offset");
	 	 		
	 	 		offset.setAttribute("x", "-5.0");
	 	 		offset.setAttribute("y", "35.0");
	 	 		
	 	 		graphics = new Element("graphics");
	 	 		graphics.addContent(offset);
	 	 		
	 	 		name.addContent(value);
	 	 		name.addContent(graphics);
	 	 		place4.addContent(name);
	 	 		
	 	 		initialMarking = new Element("initialMarking");
	 	 		value = new Element("value");
	 	  		str = "Black,0";
	 	  		 
	 	 		value.setText(str);
	 	 		
	 	 		graphics = new Element("graphics");
	 	 		offset = new Element("offset");
	 	 		offset.setAttribute("x", "0");
	 	 		offset.setAttribute("y", "0");
	 	 		graphics.addContent(offset);
	 	 		initialMarking.addContent(value);
	 	 		initialMarking.addContent(graphics);
	 	 		place4.addContent(initialMarking);
	 	 		
	 	 		capacity = new Element("capacity");
	 	 		value = new Element("value"); 
	 	 		value.setText("0");
	 	 		capacity.addContent(value);
	 	 		
	 	 		place4.addContent(capacity);
	 	 		
	 	 		net.addContent(place4);
	 	 		
	 	 		addArc( transition4, place4, net, 1,false);
	 	 		
	 	 		
	 	 		Element transition5 = new Element("transition");
	 	 		transition5.setAttribute("id", "T"+tIndex);
	 	 		graphics2 = new Element("graphics");
	 	 		transition2Position = new Element("position");
	 	 		
	 	 		transition2Position.setAttribute("x", String.valueOf(1400));//位置问题
	 	 		transition2Position.setAttribute("y", String.valueOf(100+i*150+200));
	 	 		graphics2.addContent(transition2Position);
	 	 		transition5.addContent(graphics2);
	 	 		 name2 = new Element("name");
	 	 		 value2 = new Element("value");
	 	 		
	 	 		value2.setText("T"+tIndex++);
	 	 		 offset2 = new Element("offset");
	 	 		
	 	 		offset2.setAttribute("x", "-5.0");
	 	 		offset2.setAttribute("y", "35.0");
	 	 		
	 	 		graphics2 = new Element("graphics");
	 	 		graphics2.addContent(offset2);
	 	 		
	 	 		name2.addContent(value2);
	 	 		name2.addContent(graphics2);
	 	 		transition1.addContent(name2);
	 	 		
	 	 		 orentation2 = new Element("orentation");
	 	 		value2 = new Element("value");
	 	 		 str2 ="0";
	 	 		value2.setText(str2);
	 	 		orentation2.addContent(value2);
	 	 		transition5.addContent(orentation2);
	 	 		
	 	 		 rate2 = new Element("rate");
	 	 		value2 = new Element("value");
	 	 		str2 ="0";
	 	 		value2.setText(str2);
	 	 		rate2.addContent(value2);
	 	 		transition5.addContent(rate2);
	 	 		
	 	 		 timed2 = new Element("timed");
	 	 		value2 = new Element("value");
	 	 		str2 ="false";
	 	 		value2.setText(str);
	 	 		timed2.addContent(value2);
	 	 		transition5.addContent(timed2);
	 	 		
	 	 		 deterministic2 = new Element("deterministic");
	 	 		value2 = new Element("value");
	 	 		str2 ="false";
	 	 		value2.setText(str2);
	 	 		deterministic2.addContent(value2);
	 	 		transition5.addContent(deterministic2);
	 	 		
	 	 		 infiniteServer2 = new Element("infiniteServer");
	 	 		value2 = new Element("value");
	 	 		str2 ="false";
	 	 		value2.setText(str2);
	 	 		infiniteServer2.addContent(value2);
	 	 		transition5.addContent(infiniteServer2);
	 	 		
	 	 		 priorty2 = new Element("priorty");
	 	 		value2 = new Element("value");
	 	 		str2 ="0"; 
	 	 		value2.setText(str2);
	 	 		priorty2.addContent(value2);
	 	 		transition5.addContent(priorty2);
	 	 		
	 	 		 delay2 = new Element("delay"); 
	 	 		value2 = new Element("value");
	 	 		str2 ="0";  
	 	 		value2.setText(str2);
	 	 		delay2.addContent(value2); 
	 	 		transition5.addContent(delay2); 
 	 	 		net.addContent(transition5);
	 	 		
	 	 		addArc(place4,transition5,net,1 ,false);
	 	 		
	 	 		
	 	 		Element place5 = new Element("place");
	 	 		place5.setAttribute("id", "P"+pIndex);
	 	 		graphics = new Element("graphics");
	 	 		Element placePosition5 = new Element("position");
	 	 		
	 	 		placePosition5.setAttribute("x", String.valueOf(950));
	 	 		placePosition5.setAttribute("y", String.valueOf(100+i*150+200));
	 	 		graphics.addContent(placePosition5);
	 	 		place5.addContent(graphics);
	 	 		
	 	 		name = new Element("name");
	 	 		value = new Element("value");
	 	 		
	 	 		value.setText("P"+pIndex++);
	 	 		offset = new Element("offset");
	 	 		
	 	 		offset.setAttribute("x", "-5.0");
	 	 		offset.setAttribute("y", "35.0");
	 	 		
	 	 		graphics = new Element("graphics");
	 	 		graphics.addContent(offset);
	 	 		
	 	 		name.addContent(value);
	 	 		name.addContent(graphics);
	 	 		place5.addContent(name);
	 	 		
	 	 		initialMarking = new Element("initialMarking");
	 	 		value = new Element("value");
	 	  		str = "Black,1";
	 	  		 
	 	 		value.setText(str);
	 	 		
	 	 		graphics = new Element("graphics");
	 	 		offset = new Element("offset");
	 	 		offset.setAttribute("x", "0");
	 	 		offset.setAttribute("y", "0");
	 	 		graphics.addContent(offset);
	 	 		initialMarking.addContent(value);
	 	 		initialMarking.addContent(graphics);
	 	 		place5.addContent(initialMarking);
	 	 		
	 	 		capacity = new Element("capacity");
	 	 		value = new Element("value"); 
	 	 		value.setText("0");
	 	 		capacity.addContent(value);
	 	 		
	 	 		place5.addContent(capacity);
	 	 		
	 	 		net.addContent(place5);
	 	 		addArc( transition5,place5, net,1,false);
	 	 		addArc( place5,transition2,net,1,false);
	 		}
	  		
	  		addArc(listOfPlacesInRtEnd.get(i),transition1,  net,1,false);
	 		
		}
		for(int i=0; i<listOfPlacesInRtEnd.size()-1; i++)
		{
			Element placei = listOfPlacesInRtEnd.get(i);
			for(int j=i+1; j<listOfScheduleStartTransition.size() ; j++)
			{
				Element transitionj = listOfScheduleStartTransition.get(j);
				addArc(placei, transitionj, net, 1, true);
			}
		}
		
	}

	private static void addRTs(GuideVO vo, Element net)
	{
 		for(int i=0; i<vo.getNumOfRTs(); i++)
 		{
 			RTInfo info = vo.getPriorityQueue().remove();
 			if(info.typeOfMessage==GuideVO.EVENT_MESSAGE)
 			{
 				addEventRt(info.id ,net,100, 100+i*150);
 			}else if(info.typeOfMessage==GuideVO.PERIOD_MESSAGE)
 			{
 				addPeriodRt(info.id , info.delay , net,100, 100+i*150);
 			}
 			
 			
 			 vo.getPriorityQueue().add(info);
 		}
	}

	private static void addEventRt(String nameOfRT, Element net ,int x, int y) 
	{
		Element place = buildPlace(net, nameOfRT, 1, x, y); 
		Element place1 = buildPlace(net, "", 0, x+390, y);
		Element transition = buildTransition(net, "", true, false, 0, 0, x+210, y);
		listOfPlacesInRtEnd.add(place1);
		/*
 		Element place = new Element("place");
 		place.setAttribute("id", nameOfRT);
 		Element graphics = new Element("graphics");
 		Element placePosition = new Element("position");
 		
 		placePosition.setAttribute("x", String.valueOf(x));
 		placePosition.setAttribute("y", String.valueOf(y));
 		graphics.addContent(placePosition);
 		place.addContent(graphics);
 		
 		Element name = new Element("name");
 		Element value = new Element("value");
 		
 		value.setText(nameOfRT);
 		Element offset = new Element("offset");
 		
 		offset.setAttribute("x", "-5.0");
 		offset.setAttribute("y", "35.0");
 		
 		graphics = new Element("graphics");
 		graphics.addContent(offset);
 		
 		name.addContent(value);
 		name.addContent(graphics);
 		place.addContent(name);
 		
 		Element initialMarking = new Element("initialMarking");
 		value = new Element("value");
  		String str = "Black,1";
  		 
 		value.setText(str);
 		
 		graphics = new Element("graphics");
 		offset = new Element("offset");
 		offset.setAttribute("x", "0");
 		offset.setAttribute("y", "0");
 		graphics.addContent(offset);
 		initialMarking.addContent(value);
 		initialMarking.addContent(graphics);
 		place.addContent(initialMarking);
 		
 		Element capacity = new Element("capacity");
 		value = new Element("value");
 		value.setText("0");
 		capacity.addContent(value);
 		
 		place.addContent(capacity);
 		
 		net.addContent(place);
 		
 		Element place1 = new Element("place");
 		place1 = new Element("place");
 		place1.setAttribute("id", "P"+pIndex);
 		listOfPlacesInRtEnd.add(place1);
 		graphics = new Element("graphics");
 		Element place1position = new Element("position");
 		
 		place1position.setAttribute("x", String.valueOf(x+150));//位置问题
 		place1position.setAttribute("y", String.valueOf(y));
 		graphics.addContent(place1position);
 		place1.addContent(graphics);
 		
 		name = new Element("name");
 		value = new Element("value");
 		
 		value.setText("P"+pIndex++);
 		offset = new Element("offset");
 		
 		offset.setAttribute("x", "-5.0");
 		offset.setAttribute("y", "35.0");
 		
 		graphics = new Element("graphics");
 		graphics.addContent(offset);
 		
 		name.addContent(value);
 		name.addContent(graphics);
 		place1.addContent(name); 
 		
 		initialMarking = new Element("initialMarking");
 		value = new Element("value");
 		str ="Black,0";
  		 
 		value.setText(str);
 		
 		graphics = new Element("graphics");
 		offset = new Element("offset");
 		offset.setAttribute("x", "0");
 		offset.setAttribute("y", "0");
 		graphics.addContent(offset);
 		initialMarking.addContent(value);
 		initialMarking.addContent(graphics);
 		place1.addContent(initialMarking);
 		
 		capacity = new Element("capacity");
 		value = new Element("value");
 		value.setText("0");
 		capacity.addContent(value);
 		place1.addContent(capacity);
 		net.addContent(place1);
 		
 		
//
 		Element transition = new Element("transition");
 		transition.setAttribute("id", "T"+tIndex);
 		graphics = new Element("graphics");
 		Element transitionPosition = new Element("position");
 		
 		transitionPosition.setAttribute("x", String.valueOf(x+75));//位置问题
 		transitionPosition.setAttribute("y", String.valueOf(y));
 		graphics.addContent(transitionPosition);
 		transition.addContent(graphics);
 		name = new Element("name");
 		value = new Element("value");
 		
 		value.setText("T"+tIndex++);
 		offset = new Element("offset");
 		
 		offset.setAttribute("x", "-5.0");
 		offset.setAttribute("y", "35.0");
 		
 		graphics = new Element("graphics");
 		graphics.addContent(offset);
 		
 		name.addContent(value);
 		name.addContent(graphics);
 		transition.addContent(name);
 		
 		Element orentation = new Element("orentation");
 		value = new Element("value");
 		str ="0";
 		value.setText(str);
 		orentation.addContent(value);
 		transition.addContent(orentation);
 		
 		Element rate = new Element("rate");
 		value = new Element("value");
 		str ="122";
 		value.setText(str);
 		rate.addContent(value);
 		transition.addContent(rate);
 		
 		Element timed = new Element("timed");
 		value = new Element("value");
 		str ="true";
 		value.setText(str);
 		timed.addContent(value);
 		transition.addContent(timed);
 		
 		Element deterministic = new Element("deterministic");
 		value = new Element("value");
 		str ="false";
 		value.setText(str);
 		deterministic.addContent(value);
 		transition.addContent(deterministic);
 		
 		Element infiniteServer = new Element("infiniteServer");
 		value = new Element("value");
 		str ="false";
 		value.setText(str);
 		infiniteServer.addContent(value);
 		transition.addContent(infiniteServer);
 		
 		Element priorty = new Element("priorty");
 		value = new Element("value");
 		str ="0";
 		value.setText(str);
 		priorty.addContent(value);
 		transition.addContent(priorty);
 		
 		Element delay = new Element("delay");
 		value = new Element("value");
 		str ="0";
 		value.setText(str);
 		delay.addContent(value);
 		transition.addContent(delay);
 		 
 		net.addContent(transition);*/
 		
 		Element arc1 = new Element("arc");
 		arc1.setAttribute("id", ""+place.getAttributeValue("id")+" to "+transition.getAttributeValue("id"));
 		arc1.setAttribute("source",place.getAttributeValue("id"));
 		arc1.setAttribute("target",transition.getAttributeValue("id"));
 		Element graphics = new Element("graphics");
 		arc1.addContent(graphics);
 		
 		Element inscription = new Element("inscription");
 		Element value = new Element("value");
 		String str ="Black,1";
 		value.setText(str);
 		inscription.addContent(value);
 		graphics = new Element("graphics");
 		inscription.addContent(graphics);
 		arc1.addContent(inscription);
 		
 		Element tagged = new Element("tagged");
 		value = new Element("value");
 		str ="false";
 		value.setText(str);
 		tagged.addContent(value);
 		arc1.addContent(tagged);
 		
 		Element arcpath = new Element("arcpath");
 		arcpath.setAttribute("id", "000");
 		arcpath.setAttribute("x", "000");
 		arcpath.setAttribute("y", "000");
 		arcpath.setAttribute("curvePoint", "false");


  		arc1.addContent(arcpath);
 		
  		arcpath = new Element("arcpath");
 		arcpath.setAttribute("id", "001");
 		arcpath.setAttribute("x", "000");
 		arcpath.setAttribute("y", "000");
 		arcpath.setAttribute("curvePoint", "false");
 		arc1.addContent(arcpath);
 		
 		Element type = new Element("type");
 		type.setAttribute("value", "normal");
  		arc1.addContent(type);
  		net.addContent(arc1);
 		
  		
  		Element arc2 = new Element("arc");
  		arc2.setAttribute("id", ""+transition.getAttributeValue("id")+" to "+place1.getAttributeValue("id"));
  		arc2.setAttribute("source",transition.getAttributeValue("id"));
  		arc2.setAttribute("target",place1.getAttributeValue("id"));
 		graphics = new Element("graphics");
 		arc2.addContent(graphics);
 		
 		inscription = new Element("inscription");
 		value = new Element("value");
 		str ="Black,1";
 		value.setText(str);
 		inscription.addContent(value);
 		graphics = new Element("graphics");
 		inscription.addContent(graphics);
 		arc2.addContent(inscription);
 		
 		tagged = new Element("tagged");
 		value = new Element("value");
 		str ="false";
 		value.setText(str);
 		tagged.addContent(value);
 		arc2.addContent(tagged);
 		
 		arcpath = new Element("arcpath");
 		arcpath.setAttribute("id", "000");
 		arcpath.setAttribute("x", "000");
 		arcpath.setAttribute("y", "000");
 		arcpath.setAttribute("curvePoint", "false");


 		arc2.addContent(arcpath);
 		
  		arcpath = new Element("arcpath");
 		arcpath.setAttribute("id", "001");
 		arcpath.setAttribute("x", "000");
 		arcpath.setAttribute("y", "000");
 		arcpath.setAttribute("curvePoint", "false");
 		arc2.addContent(arcpath);
 		
 		type = new Element("type");
 		type.setAttribute("value", "normal");
 		arc2.addContent(type);
  		net.addContent(arc2);
 		
  		Element arc3 = new Element("arc");
  		arc3.setAttribute("id", ""+transition.getAttributeValue("id")+" to "+place.getAttributeValue("id"));
  		arc3.setAttribute("source",transition.getAttributeValue("id"));
  		arc3.setAttribute("target",place.getAttributeValue("id"));
 		graphics = new Element("graphics");
 		arc3.addContent(graphics);
 		
 		inscription = new Element("inscription");
 		value = new Element("value");
 		str ="Black,1";
 		value.setText(str);
 		inscription.addContent(value);
 		graphics = new Element("graphics");
 		inscription.addContent(graphics);
 		arc3.addContent(inscription);
 		
 		tagged = new Element("tagged");
 		value = new Element("value");
 		str ="false";
 		value.setText(str);
 		tagged.addContent(value);
 		arc3.addContent(tagged);
 		
 		Element arcpath1 = new Element("arcpath");
 		arcpath1.setAttribute("id", "000");
 		arcpath1.setAttribute("x", "000");
 		arcpath1.setAttribute("y", "000");
 		arcpath1.setAttribute("curvePoint", "false");
 		arc3.addContent(arcpath1);
 		
 		Element arcpath2 = new Element("arcpath");
 		arcpath2.setAttribute("id", "000");
  		arcpath2.setAttribute("x", ""+(Integer.parseInt(transition.getChild("graphics").getChild("position").getAttributeValue("x"))+15));
 		arcpath2.setAttribute("y", ""+(Integer.parseInt(transition.getChild("graphics").getChild("position").getAttributeValue("y"))-28));
 		arcpath2.setAttribute("curvePoint", "false");

 		arc3.addContent(arcpath2);
 		
 		Element arcpath3 = new Element("arcpath");
 		arcpath3.setAttribute("id", "001");
 		arcpath3.setAttribute("x",  ""+(Integer.parseInt(place.getChild("graphics").getChild("position").getAttributeValue("x"))+15));
 		arcpath3.setAttribute("y", ""+(Integer.parseInt(transition.getChild("graphics").getChild("position").getAttributeValue("y"))-28));
 		arcpath3.setAttribute("curvePoint", "false");
 		arc3.addContent(arcpath3); 
 		
 		Element arcpath4 = new Element("arcpath");
 		arcpath4.setAttribute("id", "001");
 		arcpath4.setAttribute("x", "000");
 		arcpath4.setAttribute("y", "000");
 		arcpath4.setAttribute("curvePoint", "false");
 		arc3.addContent(arcpath4); 
 		
 		type = new Element("type");
 		type.setAttribute("value", "normal");
 		arc3.addContent(type);
  		net.addContent(arc3);
  		
  		
  		Element arc4 = new Element("arc");
  		arc4.setAttribute("id", ""+place1.getAttributeValue("id")+" to "+transition.getAttributeValue("id"));
  		arc4.setAttribute("source",place1.getAttributeValue("id"));
  		arc4.setAttribute("target",transition.getAttributeValue("id"));
 		graphics = new Element("graphics");
 		arc4.addContent(graphics);
 		
 		inscription = new Element("inscription");
 		value = new Element("value");
 		str ="Black,1";
 		value.setText(str);
 		inscription.addContent(value);
 		graphics = new Element("graphics");
 		inscription.addContent(graphics);
 		arc4.addContent(inscription);
 		
 		tagged = new Element("tagged");
 		value = new Element("value");
 		str ="false";
 		value.setText(str);
 		tagged.addContent(value);
 		arc4.addContent(tagged);
 		
 		arcpath1 = new Element("arcpath");
 		arcpath1.setAttribute("id", "000");
 		arcpath1.setAttribute("x", "000");
 		arcpath1.setAttribute("y", "000");
 		arcpath1.setAttribute("curvePoint", "false");
 		arc4.addContent(arcpath1);
 		
 		arcpath2 = new Element("arcpath");
 		arcpath2.setAttribute("id", "000");
 		arcpath2.setAttribute("x", ""+(Integer.parseInt(place1.getChild("graphics").getChild("position").getAttributeValue("x"))+15));
 		arcpath2.setAttribute("y", ""+(Integer.parseInt(transition.getChild("graphics").getChild("position").getAttributeValue("y"))+58)); 
 		arcpath2.setAttribute("curvePoint", "false");

 		arc4.addContent(arcpath2);
 		
 		arcpath3 = new Element("arcpath");
 		arcpath3.setAttribute("id", "001");
 		arcpath3.setAttribute("x",  ""+(Integer.parseInt(transition.getChild("graphics").getChild("position").getAttributeValue("x"))+15));
 		arcpath3.setAttribute("y", ""+(Integer.parseInt(transition.getChild("graphics").getChild("position").getAttributeValue("y"))+58));
 		arcpath3.setAttribute("curvePoint", "false");
 		arc4.addContent(arcpath3); 
 		
 		arcpath4 = new Element("arcpath");
 		arcpath4.setAttribute("id", "001");
 		arcpath4.setAttribute("x", "000");
 		arcpath4.setAttribute("y", "000");
 		arcpath4.setAttribute("curvePoint", "false");
 		arc4.addContent(arcpath4); 
 		
 		type = new Element("type");
 		type.setAttribute("value", "inhibitor");
 		arc4.addContent(type);
  		net.addContent(arc4);
  		
	}
	
	//delay 是周期
	private static void addPeriodRt(String nameOfRT, int delayOfRT, Element net, int x, int y) {
		Element place0 = buildPlace(net, nameOfRT, 1, x, y); 	  
// 		Element place0 = new Element("place");
//		place0.setAttribute("id", nameOfRT);
// 		Element graphics = new Element("graphics");
// 		Element place0Position = new Element("position");
// 		
// 		place0Position.setAttribute("x", String.valueOf(x)); 
// 		place0Position.setAttribute("y", String.valueOf(y));
// 		graphics.addContent(place0Position);
// 		place0.addContent(graphics);
// 		
// 		Element name = new Element("name");
// 		Element value = new Element("value");
// 		
// 		value.setText(nameOfRT);
// 		Element offset = new Element("offset");
// 		
// 		offset.setAttribute("x", "-5.0");
// 		offset.setAttribute("y", "35.0");
// 		
// 		graphics = new Element("graphics");
// 		graphics.addContent(offset);
// 		
// 		name.addContent(value);
// 		name.addContent(graphics);
// 		place0.addContent(name);
// 		
// 		Element initialMarking = new Element("initialMarking");
// 		value = new Element("value");
//  		String str = "Black,1";
//  		 
// 		value.setText(str);
// 		
// 		graphics = new Element("graphics");
// 		offset = new Element("offset");
// 		offset.setAttribute("x", "0");
// 		offset.setAttribute("y", "0");
// 		graphics.addContent(offset);
// 		initialMarking.addContent(value);
// 		initialMarking.addContent(graphics);
// 		place0.addContent(initialMarking);
// 		
// 		Element capacity = new Element("capacity");
// 		value = new Element("value");
// 		value.setText("0");
// 		capacity.addContent(value);
// 		
// 		place0.addContent(capacity);
// 		
// 		net.addContent(place0); 
 		
		Element place1 = buildPlace(net, "", 0, x+150, y); 
// 		Element place1 = new Element("place");
// 		place1.setAttribute("id", "P"+pIndex);
// 		listOfPlacesInRtEnd.add(place1);
// 		graphics = new Element("graphics");
// 		Element place1Position = new Element("position");
// 		
// 		place1Position.setAttribute("x", String.valueOf(x+150)); 
// 		place1Position.setAttribute("y", String.valueOf(y));
// 		graphics.addContent(place1Position);
// 		place1.addContent(graphics);
// 		
// 		name = new Element("name");
// 		value = new Element("value");
// 		
// 		value.setText("P"+pIndex++);
// 		offset = new Element("offset");
// 		
// 		offset.setAttribute("x", "-5.0");
// 		offset.setAttribute("y", "35.0");
// 		
// 		graphics = new Element("graphics");
// 		graphics.addContent(offset);
// 		
// 		name.addContent(value);
// 		name.addContent(graphics);
// 		place1.addContent(name);
// 		
// 		initialMarking = new Element("initialMarking");
// 		value = new Element("value");
//  		str = "Black,0";
//  		 
// 		value.setText(str);
// 		
// 		graphics = new Element("graphics");
// 		offset = new Element("offset");
// 		offset.setAttribute("x", "0");
// 		offset.setAttribute("y", "0");
// 		graphics.addContent(offset);
// 		initialMarking.addContent(value);
// 		initialMarking.addContent(graphics);
// 		place1.addContent(initialMarking);
// 		
// 		capacity = new Element("capacity");
// 		value = new Element("value");
// 		value.setText("0");
// 		capacity.addContent(value);
// 		
// 		place1.addContent(capacity);
// 		
// 		net.addContent(place1); 
		
		Element place2 = buildPlace(net, "", 0, x+200, y+40);
		
		
		
		Element transition0 = buildTransition(net, "", false, true, 100, 0, x+75, y+40);
		addArc( transition0, place2,net ,1,false);
		
		Element transition2 = buildTransition(net, "", false, false, 0, 0, x+300, y+40);
		addArc(place2, transition2,   net , delayOfRT/100,false);
		
		Element place3 = buildPlace(net, "", 0, x+400, y+40);
		listOfPlacesInRtEnd.add(place3);
		addArc(transition2,place3,  net,1,false);
		
		
// 		Element transition0 = new Element("transition");
// 		transition0.setAttribute("id", "T"+tIndex);
// 		graphics = new Element("graphics");
// 		Element transitionPosition = new Element("position");
// 		
// 		transitionPosition.setAttribute("x", String.valueOf(x+75));//位置问题
// 		transitionPosition.setAttribute("y", String.valueOf(y+40));
// 		graphics.addContent(transitionPosition);
// 		transition0.addContent(graphics);
// 		name = new Element("name");
// 		value = new Element("value");
// 		
// 		value.setText("T"+tIndex++);
// 		offset = new Element("offset");
// 		
// 		offset.setAttribute("x", "-5.0");
// 		offset.setAttribute("y", "35.0");
// 		
// 		graphics = new Element("graphics");
// 		graphics.addContent(offset);
// 		
// 		name.addContent(value);
// 		name.addContent(graphics);
// 		transition0.addContent(name);
// 		
// 		Element orentation = new Element("orentation");
// 		value = new Element("value");
// 		str ="0";
// 		value.setText(str);
// 		orentation.addContent(value);
// 		transition0.addContent(orentation);
// 		
// 		Element rate = new Element("rate");
// 		value = new Element("value");
// 		str ="0";
// 		value.setText(str);
// 		rate.addContent(value);
// 		transition0.addContent(rate);
// 		
// 		Element timed = new Element("timed");
// 		value = new Element("value");
// 		str ="false";
// 		value.setText(str);
// 		timed.addContent(value);
// 		transition0.addContent(timed);
// 		
// 		Element deterministic = new Element("deterministic");
// 		value = new Element("value");
// 		str ="true";
// 		value.setText(str);
// 		deterministic.addContent(value);
// 		transition0.addContent(deterministic);
// 		
// 		Element infiniteServer = new Element("infiniteServer");
// 		value = new Element("value");
// 		str ="false";
// 		value.setText(str);
// 		infiniteServer.addContent(value);
// 		transition0.addContent(infiniteServer);
// 		
// 		Element priorty = new Element("priorty");
// 		value = new Element("value");
// 		str ="0"; 
// 		value.setText(str);
// 		priorty.addContent(value);
// 		transition0.addContent(priorty);
// 		
// 		Element delay = new Element("delay"); 
// 		value = new Element("value");
// 		str ="10";  
// 		value.setText(str);
// 		delay.addContent(value); 
// 		transition0.addContent(delay); 
// 		 
// 		net.addContent(transition0);
 		
		Element transition1 = buildTransition(net, "", false, false, 100, 0, x+75, y-40);
		
// 		Element transition1 = new Element("transition");
// 		transition1.setAttribute("id", "T"+tIndex);
// 		graphics = new Element("graphics");
// 		Element transition1Position = new Element("position");
// 		
// 		transition1Position.setAttribute("x", String.valueOf(x+75));//位置问题
// 		transition1Position.setAttribute("y", String.valueOf(y-40));
// 		graphics.addContent(transition1Position);
// 		transition1.addContent(graphics);
// 		name = new Element("name");
// 		value = new Element("value");
// 		
// 		value.setText("T"+tIndex++);
// 		offset = new Element("offset");
// 		
// 		offset.setAttribute("x", "-5.0");
// 		offset.setAttribute("y", "35.0");
// 		
// 		graphics = new Element("graphics");
// 		graphics.addContent(offset);
// 		
// 		name.addContent(value);
// 		name.addContent(graphics);
// 		transition1.addContent(name);
// 		
// 		orentation = new Element("orentation");
// 		value = new Element("value");
// 		str ="0";
// 		value.setText(str);
// 		orentation.addContent(value);
// 		transition1.addContent(orentation);
// 		
// 		rate = new Element("rate");
// 		value = new Element("value");
// 		str ="0";
// 		value.setText(str);
// 		rate.addContent(value);
// 		transition1.addContent(rate);
// 		
// 		timed = new Element("timed");
// 		value = new Element("value");
// 		str ="false";
// 		value.setText(str);
// 		timed.addContent(value);
// 		transition1.addContent(timed);
// 		
// 		deterministic = new Element("deterministic");
// 		value = new Element("value");
// 		str ="false";
// 		value.setText(str);
// 		deterministic.addContent(value);
// 		transition1.addContent(deterministic);
// 		
// 		infiniteServer = new Element("infiniteServer");
// 		value = new Element("value");
// 		str ="false";
// 		value.setText(str);
// 		infiniteServer.addContent(value);
// 		transition1.addContent(infiniteServer);
// 		
// 		priorty = new Element("priorty");
// 		value = new Element("value");
// 		str ="0"; 
// 		value.setText(str);
// 		priorty.addContent(value);
// 		transition1.addContent(priorty);
// 		
// 		delay = new Element("delay"); 
// 		value = new Element("value");
// 		str ="0";  
// 		value.setText(str);
// 		delay.addContent(value); 
// 		transition1.addContent(delay); 
// 		 
// 		net.addContent(transition1);
// 		
 		addArc(place0, transition0, net, 1,false);
 		addArc(transition0, place1, net, 1,false);
 		addArc(place1, transition1, net, 1,false);
 		addArc(transition1, place0, net, 1,false);
// 		Element arc1 = new Element("arc");
// 		arc1.setAttribute("id", ""+place0.getAttributeValue("id")+" to "+transition0.getAttributeValue("id"));
// 		arc1.setAttribute("source",place0.getAttributeValue("id"));
// 		arc1.setAttribute("target",transition0.getAttributeValue("id"));
// 		Element graphics = new Element("graphics");
// 		arc1.addContent(graphics);
// 		
// 		Element inscription = new Element("inscription");
// 		Element value = new Element("value");
// 		String str ="Black,1";
// 		value.setText(str);
// 		inscription.addContent(value);
// 		graphics = new Element("graphics");
// 		inscription.addContent(graphics);
// 		arc1.addContent(inscription);
// 		
// 		Element tagged = new Element("tagged");
// 		value = new Element("value");
// 		str ="false";
// 		value.setText(str);
// 		tagged.addContent(value);
// 		arc1.addContent(tagged);
// 		
// 		Element arcpath = new Element("arcpath");
// 		arcpath.setAttribute("id", "000");
// 		arcpath.setAttribute("x", "000");
// 		arcpath.setAttribute("y", "000");
// 		arcpath.setAttribute("curvePoint", "false");
//
//
//  		arc1.addContent(arcpath);
// 		
//  		arcpath = new Element("arcpath");
// 		arcpath.setAttribute("id", "001");
// 		arcpath.setAttribute("x", "000");
// 		arcpath.setAttribute("y", "000");
// 		arcpath.setAttribute("curvePoint", "false");
// 		arc1.addContent(arcpath);
// 		
// 		Element type = new Element("type");
// 		type.setAttribute("value", "normal");
//  		arc1.addContent(type);
//  		net.addContent(arc1);
//  		
//  		Element arc2 = new Element("arc");
//  		arc2.setAttribute("id", ""+transition0.getAttributeValue("id")+" to "+place1.getAttributeValue("id"));
//  		arc2.setAttribute("source",transition0.getAttributeValue("id"));
//  		arc2.setAttribute("target",place1.getAttributeValue("id"));
// 		graphics = new Element("graphics");
// 		arc2.addContent(graphics);
// 		
// 		inscription = new Element("inscription");
// 		value = new Element("value");
// 		str ="Black,1";
// 		value.setText(str);
// 		inscription.addContent(value);
// 		graphics = new Element("graphics");
// 		inscription.addContent(graphics);
// 		arc2.addContent(inscription);
// 		
// 		tagged = new Element("tagged");
// 		value = new Element("value");
// 		str ="false";
// 		value.setText(str);
// 		tagged.addContent(value);
// 		arc2.addContent(tagged);
// 		
// 		arcpath = new Element("arcpath");
// 		arcpath.setAttribute("id", "000");
// 		arcpath.setAttribute("x", "000");
// 		arcpath.setAttribute("y", "000");
// 		arcpath.setAttribute("curvePoint", "false");
//
//
// 		arc2.addContent(arcpath);
// 		
//  		arcpath = new Element("arcpath");
// 		arcpath.setAttribute("id", "001");
// 		arcpath.setAttribute("x", "000");
// 		arcpath.setAttribute("y", "000");
// 		arcpath.setAttribute("curvePoint", "false");
// 		arc2.addContent(arcpath);
// 		
// 		type = new Element("type");
// 		type.setAttribute("value", "normal");
// 		arc2.addContent(type);
//  		net.addContent(arc2);
// 		
//  		Element arc3 = new Element("arc");
//  		arc3.setAttribute("id", ""+place1.getAttributeValue("id")+" to "+transition1.getAttributeValue("id"));
//  		arc3.setAttribute("source",transition1.getAttributeValue("id"));
//  		arc3.setAttribute("target",place1.getAttributeValue("id"));
// 		graphics = new Element("graphics");
// 		arc3.addContent(graphics);
// 		
// 		inscription = new Element("inscription");
// 		value = new Element("value");
// 		str ="Black,1";
// 		value.setText(str);
// 		inscription.addContent(value);
// 		graphics = new Element("graphics");
// 		inscription.addContent(graphics);
// 		arc3.addContent(inscription);
// 		
// 		tagged = new Element("tagged");
// 		value = new Element("value");
// 		str ="false";
// 		value.setText(str);
// 		tagged.addContent(value);
// 		arc3.addContent(tagged);
// 		
// 		Element arcpath1 = new Element("arcpath");
// 		arcpath1.setAttribute("id", "000");
// 		arcpath1.setAttribute("x", "000");
// 		arcpath1.setAttribute("y", "000");
// 		arcpath1.setAttribute("curvePoint", "false");
// 		arc3.addContent(arcpath1);
// 		
// 		Element arcpath4 = new Element("arcpath");
// 		arcpath4.setAttribute("id", "001");
// 		arcpath4.setAttribute("x", "000");
// 		arcpath4.setAttribute("y", "000");
// 		arcpath4.setAttribute("curvePoint", "false");
// 		arc3.addContent(arcpath4); 
// 		
// 		type = new Element("type");
// 		type.setAttribute("value", "normal");
// 		arc3.addContent(type);
//  		net.addContent(arc3);
//  		
//  		
//  		Element arc4 = new Element("arc");
//  		arc4.setAttribute("id", ""+transition1.getAttributeValue("id")+" to "+place0.getAttributeValue("id"));
//  		arc4.setAttribute("source",transition1.getAttributeValue("id"));
//  		arc4.setAttribute("target",place0.getAttributeValue("id"));
// 		graphics = new Element("graphics");
// 		arc4.addContent(graphics);
// 		
// 		inscription = new Element("inscription");
// 		value = new Element("value");
// 		str ="Black,1";
// 		value.setText(str);
// 		inscription.addContent(value);
// 		graphics = new Element("graphics");
// 		inscription.addContent(graphics);
// 		arc4.addContent(inscription);
// 		
// 		tagged = new Element("tagged");
// 		value = new Element("value");
// 		str ="false";
// 		value.setText(str);
// 		tagged.addContent(value);
// 		arc4.addContent(tagged);
// 		
// 		arcpath1 = new Element("arcpath");
// 		arcpath1.setAttribute("id", "000");
// 		arcpath1.setAttribute("x", "000");
// 		arcpath1.setAttribute("y", "000");
// 		arcpath1.setAttribute("curvePoint", "false");
// 		arc4.addContent(arcpath1);
// 		
// 		arcpath4 = new Element("arcpath");
// 		arcpath4.setAttribute("id", "001");
// 		arcpath4.setAttribute("x", "000");
// 		arcpath4.setAttribute("y", "000");
// 		arcpath4.setAttribute("curvePoint", "false");
// 		arc4.addContent(arcpath4); 
// 		
// 		type = new Element("type");
// 		type.setAttribute("value", "normal");
// 		arc4.addContent(type);
// 		
// 		
//  		net.addContent(arc4);
 		
 		
	}
	
	/**
	 * 构造一个place 并且加在net上面
	 * @param net 整个的网
	 * @param placeName  若有名字 给名字，若为空 则用 默认的编号P1 P2等
	 * @param initailMark  是否有初始的标记
	 * @param x 坐标
	 * @param y
	 * @return place
	 */
	private static Element buildPlace(Element net, String placeName ,int initailMark, int x, int y)
	{
		
		Element place = new Element("place");
//		place.setAttribute("id", "P"+pIndex);
		if(placeName=="")
			place.setAttribute("id", "P"+pIndex);
		else
			place.setAttribute("id", placeName);
 		 
		Element graphics = new Element("graphics");
 		Element position = new Element("position");
// 		position.setAttribute("x", String.valueOf(x+150)); 
// 		position.setAttribute("y", String.valueOf(y));
 		position.setAttribute("x", String.valueOf(x)); 
 		position.setAttribute("y", String.valueOf(y));
 		graphics.addContent(position);
 		place.addContent(graphics);
 		Element name = new Element("name");
 		Element value = new Element("value");
 		value.setText("P"+pIndex++);
 		if(placeName=="")
 			value.setText("P"+pIndex++);
		else
			value.setText(placeName);
 		Element offset = new Element("offset");
 		offset.setAttribute("x", "-5.0");
 		offset.setAttribute("y", "35.0");
 		graphics = new Element("graphics");
 		graphics.addContent(offset);
 		name.addContent(value);
 		name.addContent(graphics);
 		place.addContent(name);
 		Element initialMarking = new Element("initialMarking");
 		value = new Element("value"); 
  		String str = "Black,"+initailMark;
 		value.setText(str);
 		graphics = new Element("graphics");
 		offset = new Element("offset");
 		offset.setAttribute("x", "0");
 		offset.setAttribute("y", "0");
 		graphics.addContent(offset);
 		initialMarking.addContent(value);
 		initialMarking.addContent(graphics);
 		place.addContent(initialMarking);
 		Element capacity = new Element("capacity");
 		value = new Element("value");
 		value.setText("0");
 		capacity.addContent(value);
 		place.addContent(capacity);
 		net.addContent(place); 
		return place;
	}
	
	/**
	 * 构造一个变迁 并且加载net上面
	 * @param net
	 * @param transitionName
	 * @param timed
	 * @param deterministed
	 * @param delay
	 * @param angle  角度
	 * @param x
	 * @param y
	 * @return
	 */
	private static Element buildTransition(Element net, String transitionName ,boolean isTimed, boolean isDeterministed, int period, int angle, int x, int y)
	{
		Element transition = new Element("transition");
		if(transitionName=="")
			transition.setAttribute("id", "T"+tIndex);
		else
			transition.setAttribute("id", transitionName);
		Element graphics = new Element("graphics");
 		Element position = new Element("position");
 		
 		position.setAttribute("x", String.valueOf(x));
 		position.setAttribute("y", String.valueOf(y));
 		graphics.addContent(position);
 		transition.addContent(graphics);
 		Element name = new Element("name");
 		Element value = new Element("value");
 		
 		if(transitionName=="")
 			value.setText("T"+tIndex++);
		else
			value.setText(transitionName);
 		
 		Element offset = new Element("offset");
 		
 		offset.setAttribute("x", "-5.0");
 		offset.setAttribute("y", "35.0");
 		
 		graphics = new Element("graphics");
 		graphics.addContent(offset);
 		
 		name.addContent(value);
 		name.addContent(graphics);
 		transition.addContent(name);
 		
 		Element orentation = new Element("orentation");
 		value = new Element("value");
 		String str ="0";
 		value.setText(str);
 		orentation.addContent(value);
 		transition.addContent(orentation);
 		
 		Element rate = new Element("rate");
 		value = new Element("value");
 		str ="0";
 		value.setText(str);
 		rate.addContent(value);
 		transition.addContent(rate);
 		
 		Element timed = new Element("timed");
 		value = new Element("value");
 		str = ""+isTimed;
  		value.setText(str);
 		timed.addContent(value);
 		transition.addContent(timed);
 		
 		Element deterministic = new Element("deterministic");
 		value = new Element("value");
 		str = ""+isDeterministed;
 		value.setText(str);
 		deterministic.addContent(value);
 		transition.addContent(deterministic);
 		
 		Element infiniteServer = new Element("infiniteServer");
 		value = new Element("value");
 		str ="false";
 		value.setText(str);
 		infiniteServer.addContent(value);
 		transition.addContent(infiniteServer);
 		
 		Element priorty = new Element("priorty");
 		value = new Element("value");
 		str ="0"; 
 		value.setText(str);
 		priorty.addContent(value);
 		transition.addContent(priorty);
 		
 		Element delay = new Element("delay"); 
 		value = new Element("value");
 		str =""+period;  
 		value.setText(str);
 		delay.addContent(value); 
 		transition.addContent(delay); 
 		 
 		net.addContent(transition);
 		
 		return transition;
	}

	public static void main(String[] args) throws Exception
	{ 
		//测试一组
		GuideVO vo = new GuideVO();
		vo.setNumOfRTs(4);
		vo.setWayOfSchedule(GuideVO.PRIORITY);
		vo.setTypeOfBus(GuideVO.THE_AFDX);
		
		Queue<RTInfo> priorityQueue = new LinkedList<RTInfo>();
		RTInfo info1 = new RTInfo("1", GuideVO.EVENT_MESSAGE, 0);
		RTInfo info2 = new RTInfo("2", GuideVO.PERIOD_MESSAGE, 300);
		RTInfo info3 = new RTInfo("3", GuideVO.PERIOD_MESSAGE, 400);
//		
		RTInfo info4 = new RTInfo("4", GuideVO.PERIOD_MESSAGE, 300);
		/*RTInfo info5 = new RTInfo("5", GuideVO.EVENT_MESSAGE, 40);
		
		RTInfo info6 = new RTInfo("6", GuideVO.EVENT_MESSAGE, 30);
		RTInfo info7 = new RTInfo("7", GuideVO.EVENT_MESSAGE, 40);*/
		priorityQueue.add(info1);
		priorityQueue.add(info2);
		priorityQueue.add(info3);
		
		priorityQueue.add(info4);
		/*	priorityQueue.add(info5);
		priorityQueue.add(info6);
		priorityQueue.add(info7);*/
		
		vo.setPriorityQueue(priorityQueue);
		
		ModelFactory.buildModel(vo);
		
	 
		
	}
	 
	private static int pIndex=0;
	private static int tIndex=0;
	private static List<Element> listOfPlacesInRtEnd = new LinkedList<Element>();
	private static List<Element> listOfScheduleStartTransition = new LinkedList<Element>();
}
