package pipe.gui.widgets.avionic;

import java.util.LinkedList;
import java.util.Queue;

public class GuideVO {
/**
 * 建模向导 传递的VO 把前后的参数串联起来 最后所有信息都收集满了 生成模型
 * @author Jerry-wang
 * 
 */
	
	private int typeOfBus = 0;
	private int numOfRTs = 1;
	private Queue<RTInfo> priorityQueue = new LinkedList<RTInfo>(); //直接顺序就代表了 优先级了
	private int wayOfSchedule = 101;
	
	
	GuideVO()
	{
		
	}
	
	public final static int THE_AFDX = 101;
	public final static int THE_1553B = 102;
	public final static int THE_CAN = 103;
	
	public final static int PRIORITY = 201;
	public final static int FIFO = 202;
	public final static int ONE_BY_ONE = 203;
	
	public final static int EVENT_MESSAGE = 301;
	public final static int PERIOD_MESSAGE = 302;
	
	static class RTInfo{
		public RTInfo(String id, int typeOfMessage, int delay) {
			super();
			this.id = id;
			this.typeOfMessage = typeOfMessage;
			this.delay = delay;
		}
		String id;
		int typeOfMessage;
		int delay;
		String remark; 
	}

	public int getTypeOfBus() {
		return typeOfBus;
	}


	public void setTypeOfBus(int typeOfBus) {
		this.typeOfBus = typeOfBus;
	}

	public int getNumOfRTs() {
		return numOfRTs;
	}

	public void setNumOfRTs(int numOfRTs) {
		this.numOfRTs = numOfRTs;
	}

	public Queue<RTInfo> getPriorityQueue() {
		return priorityQueue;
	}

	public void setPriorityQueue(Queue<RTInfo> priorityQueue) {
		this.priorityQueue = priorityQueue;
	}

	public int getWayOfSchedule() {
		return wayOfSchedule;
	}

	public void setWayOfSchedule(int wayOfSchedule) {
		this.wayOfSchedule = wayOfSchedule;
	}


	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		//这里不确定要不要做点什么了。。。
	 
		
        GuideVO newVo = new GuideVO();  
        newVo.typeOfBus=this.typeOfBus; 
		newVo.numOfRTs=this.numOfRTs; 
		newVo.wayOfSchedule=this.wayOfSchedule; 
		
		newVo.priorityQueue=this.priorityQueue;//将clone进行到底    
        return newVo;  
 	}


	@Override
	public String toString() {
		String strQueue = "\n ";
		for(int i=0; i<this.numOfRTs; i++)
		{
			RTInfo info = priorityQueue.remove();
			strQueue+="~~"+info.id+"-"+info.typeOfMessage+"-"+info.delay+"~~";
			priorityQueue.add(info);
		}
 		return "^^^^^^^^^^^"+typeOfBus+"|"+numOfRTs+"|"+wayOfSchedule+strQueue;
	}
	
	
	
	
}
