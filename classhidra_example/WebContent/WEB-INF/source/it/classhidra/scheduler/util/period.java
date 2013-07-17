package it.classhidra.scheduler.util;

import it.classhidra.core.tool.util.util_format;

import java.util.Date;

public class period {
	public int[] increment = new int[5];
	public int[] nextTime = new int[5];
	public int[] fixedTime = new int[5];

	public period(){
		super();
		init();
	}

	private void init(){
/*		
		increment[0]=0;
		increment[1]=12;
		increment[2]=1;
		increment[3]=7;
		increment[4]=24;
		increment[5]=60;

		nextTime[0]=0;
		nextTime[1]=1;
		nextTime[2]=1;
		nextTime[3]=0;
		nextTime[4]=0;
		nextTime[5]=0;

		fixedTime[0]=-1;
		fixedTime[1]=-1;
		fixedTime[2]=-1;
		fixedTime[3]=-1;
		fixedTime[4]=-1;
		fixedTime[5]=-1;
*/		
		increment[0]=0;
		increment[1]=12;
		increment[2]=1;
		increment[3]=24;
		increment[4]=60;

		nextTime[0]=0;
		nextTime[1]=1;
		nextTime[2]=1;
		nextTime[3]=0;
		nextTime[4]=0;

		fixedTime[0]=-1;
		fixedTime[1]=-1;
		fixedTime[2]=-1;
		fixedTime[3]=-1;
		fixedTime[4]=-1;

	}

	public void setIncrement(int current, int value, String currentTime){
		try{
			int prevTime=nextTime[current];
			Date prev = util_format.stringToData(toString(), "yyyy-MM-dd-HH-mm");
			nextTime[current]=value;
			Date curr = util_format.stringToData(currentTime, "yyyy-MM-dd-HH-mm");
			if(prev.getTime()>curr.getTime()){

			}else{
				if(fixedTime[current]!=-1 || prevTime==0) nextTime[current]=value;
				else nextTime[current]=prevTime;
				if(current==0){

				}else{
					if(fixedTime[current-1]==-1) nextTime[current]+=increment[current];
					else
						if(current-2>-1 && fixedTime[current-2]==-1) nextTime[current-1]+=increment[current-1];
					else
						if(current-3>-1 && fixedTime[current-3]==-1) nextTime[current-2]+=increment[current-2];
					else
						if(current-4>-1 && fixedTime[current-4]==-1) nextTime[current-3]+=increment[current-3];
/*
					else
						if(current-5>-1 && fixedTime[current-5]==-1) nextTime[current-4]+=increment[current-4];
*/
				}
			}
		}catch(Exception e){

		}

	}

	public void setDefault(int current, int value, String currentTime){
		if(current==0){
			nextTime[current]=value;
		}else{
			try{
				Date prev = util_format.stringToData(toString(), "yyyy-MM-dd-HH-mm");
				int prevTime=nextTime[current];
				nextTime[current]=value;
				Date curr = util_format.stringToData(currentTime, "yyyy-MM-dd-HH-mm");
				if(prev.getTime()>curr.getTime()) nextTime[current]=prevTime;
			}catch(Exception e){

			}

		}

	}

	public String toString(){
		String result="";
		for(int i=0;i<nextTime.length;i++){
			String prefix="-";
			if(i==0) prefix="";
			result+=prefix+nextTime[i];
		}
		return result;
	}

	public String toNewTime(){
		String result=toString();
/*
		try{
			Date curr = util_format.stringToData(toString(), "yyyy-MM-dd-HH-mm");
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(curr.getTime());

			if(calendar.get(Calendar.DAY_OF_WEEK)!=nextTime[3]){
				while(calendar.get(Calendar.DAY_OF_WEEK)<nextTime[3]){
					calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+1);
				}
				return util_format.dataToString(new Date(calendar.getTimeInMillis()), "yyyy-MM-dd-HH-mm");
			}
		}catch(Exception e){

		}
*/
		try{
			result = util_format.dataToString(util_format.stringToData(result, "yyyy-MM-dd-HH-mm"), "yyyy-MM-dd-HH-mm");
		}catch(Exception e){
		}
		return result;
	}
}
