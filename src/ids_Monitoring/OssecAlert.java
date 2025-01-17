package ids_Monitoring;

public class OssecAlert {
	
	private Rules rule;
	private String id;
	private String TimeStamp;
	private String decoder;
	private String location;
	private String full_log;
	private String hostname;
	private String name;
	private String agent_name;
	private String timestamp;
	private String logfile;


	public void setName(String newName) {
		this.name = newName;
	}
	public String getName() {
	    return name;
	}
	public String getRuleComment() {
	    return rule.getComment();
	}
	public int getRuleSidid() {
	    return rule.getSidid();
	}
	public String getfull_log() {
	    return full_log;
	}
	
	public String getID() {
	    return id;
	}
 	public class Rules {
		private int level;
		private String comment;
		private int sidid;
		private int firedtimes;
		
		public String getComment() {
			return comment;
		}
		public int getSidid() {
			return sidid;
		}
		public int getLevel() {
			return level;
		}
 	}
}

