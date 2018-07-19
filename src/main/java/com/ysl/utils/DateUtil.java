package com.ysl.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期工具类
 */
public final class DateUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class.getName());
	
	public static final DateTimeFormatter YMD = DateTimeFormat.forPattern("yyyy-MM-dd");
    public static final DateTimeFormatter YMDH = DateTimeFormat.forPattern("yyyy-MM-ddHH");
    public static final DateTimeFormatter YMDHM = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
    
    public static final DateTimeFormatter SDF = YMDHM;
	public static final DateTimeFormatter SDFSIMPLE = YMD;

    public static String getCurrentDay() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str = format.format(date);
        return str;
    }

    /**
     * 格式化日期
     * @param time 日期 yyyy-MM-dd or yyyy-MM-dd HH:mm:ss
     * @return 日期字符串 yyyyMMdd or yyyyMMddHHmmss
     * @author xsj
     * @date 2017-11-2
     */
    public static String convertDate(String time) {
        StringBuilder buf = new StringBuilder();
        int len = time.length();
        for (int i = 0; i < len; i++) {
            char c = time.charAt(i);
            switch (c) {
            case '-':
                break;
            case ':':
                break;
            case ' ':
                break;
            default:
                buf.append(c);
            }
        }
        return buf.toString();
    }
    
  public static int compare_date(String date, String other,String format) {
        try {
        	if(date!=null && other!=null){
	            Date dt1 = parseDate(date, format);
	            Date dt2 = parseDate(other,format);
	            if (dt1.getTime() > dt2.getTime()) {
	                return 1;
	            } else if (dt1.getTime() < dt2.getTime()) {
	                return -1;
	            } else {
	                return 0;
	            }
        	}else{
        		return 0;
        	}
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

	/*
	 * 获取今日时间 格式 yyyy-dd-mm 按照0-24为一天 
	 */
	public static String getTodayKey(){
     	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM");
     	return sdf.format(new Date());
		
	}

    /**
     * 某一天的前面一天
     * @param date
     * @return
     */
   public static String  getPreDay(Date date){
    	Calendar c = Calendar.getInstance();  
       
        c.setTime(date);
        int day = c.get(Calendar.DATE);  
        c.set(Calendar.DATE, day - 1);  
  
        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());  
        return dayBefore;
    }

    public static final String DEF_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 字符串轉日期
	 * @param date
	 * @param format
	 * @return
	 * @throws Exception
	 */
    public static Date str2Date(String date, String format) throws Exception {
		if (StringUtils.isBlank(format)) {
			format = DEF_DATE_FORMAT;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
   		return sdf.parse(date);
	}

	/**
	 * 字符串轉日期
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static Date str2Date(String date) throws Exception {
		return str2Date(date, DEF_DATE_FORMAT);
	}

	/**
	 * 判断时间是否在某個时间段内
	 * @param nowTime
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public static boolean isInDate(Date nowTime, Date beginTime, Date endTime) throws Exception {
		Calendar date = Calendar.getInstance();
		date.setTime(nowTime);
		Calendar begin = Calendar.getInstance();
		begin.setTime(beginTime);
		Calendar end = Calendar.getInstance();
		end.setTime(endTime);
		if (date.after(begin) && date.before(end)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 日期轉字符串
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static String date2Str (Date date) throws Exception {
		return date2Str(date,DEF_DATE_FORMAT);
	}

	/**
	 * 日期轉字符串
	 * @param date
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static String date2Str (Date date, String format) throws Exception {
		if (StringUtils.isBlank(format)) {
			format = DEF_DATE_FORMAT;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(date);
	}

	/**
	 * 获取即时盘开始时间根据赛事的开赛时间
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static String getInstantDishStartDateByMatchDate(Date date) throws Exception{
		String pend = date2Str(date,"yyyy-MM-dd") + " " + "10:00:00";
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(str2Date(pend));
		// 开赛时间 在当天10点之后 ：即时盘时间段：当天10点到赛事开赛时间
		Calendar matchTime = Calendar.getInstance();
		matchTime.setTime(date);
		if (matchTime.before(calendar.getTime())){
			calendar.add(Calendar.DATE,-1);
		}
		return date2Str(calendar.getTime());
	}

	public static String getEndMatchDate(Date date) throws Exception{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		return date2Str(calendar.getTime());
	}
	
	/**
     * 功能描述：按照给出格式解析出日期
     *
     * @param dateStr String 字符型日期
     * @param format  String 格式
     * @return Date 日期
     */
    public static Date parseDate(String dateStr, String format) {
        Date date = null;
        try {
            DateFormat df_parseDate = new SimpleDateFormat(format);
            date = df_parseDate.parse(dateStr);
        } catch (Exception e) {
        	logger.error("DateUtil.parseDate is Error，解析日期格式异常, dateStr:{}, format:{}", new Object[]{dateStr, format});
        }
        return date;
    }

    /**
     * 功能描述：格式化日期
     *
     * @param dateStr String 字符型日期：YYYY-MM-DD 格式
     * @return Date
     */
    public static Date parseDate(String dateStr) {
        return parseDate(dateStr, "yyyy-MM-dd");
    }

    /**
     * 功能描述：格式化输出日期
     *
     * @param date   Date 日期
     * @param format String 格式
     * @return 字符型日期
     */
    public static String format(Date date, String format) {
        String result = "";
        try {
            if (date != null) {
                DateFormat df_format = new SimpleDateFormat(format);
                result = df_format.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 功能描述：返回字符型日期
     *
     * @param date 日期
     * @return 返回字符型日期 yyyy/MM/dd 格式
     */
    public static String getDate(Date date) {
        return format(date, "yyyy/MM/dd");
    }

    /**
     * 功能描述：返回字符型时间
     *
     * @param date Date 日期
     * @return 返回字符型时间 HH:mm:ss 格式
     */
    public static String getTime(Date date) {
        return format(date, "HH:mm:ss");
    }

    /**
     * 功能描述：返回字符型日期时间
     *
     * @param date Date 日期
     * @return 返回字符型日期时间 yyyy/MM/dd HH:mm:ss 格式
     */
    public static String getDateTime(Date date) {
        return format(null, "yyyy/MM/dd HH:mm:ss");
    }

    public static String getMillisDateTime(Date date) {
        return format(date, "yyyy/MM/dd HH:mm:ss.SSS");
    }


    /**
     * 功能描述：取得指定月份的第一天
     *
     * @param strdate String 字符型日期
     * @return String yyyy-MM-dd 格式
     */
    public static String getMonthBegin(String strdate) {
        Date date = parseDate(strdate);
        return format(date, "yyyy-MM") + "-01";
    }


    /**
     * 功能描述：常用的格式化日期
     *
     * @param date Date 日期
     * @return String 日期字符串 yyyy-MM-dd格式
     */
    public static String formatDate(Date date) {
        return formatDateByFormat(date, "yyyy-MM-dd");
    }

    /**
     * 功能描述：以指定的格式来格式化日期
     *
     * @param date   Date 日期
     * @param format String 格式
     * @return String 日期字符串
     */
    public static String formatDateByFormat(Date date, String format) {
        String result = "";
        if (date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                result = sdf.format(date);
            } catch (Exception ignored) {
            }
        }
        return result;
    }

    /**
     * 计算2个日期之间的相隔天数
     *
     * @param d1 日期1
     * @param d2 日期2
     * @return 日期1和日期2相隔天数
     */
    public int getDaysBetween(Calendar d1, Calendar d2) {
        if (d1.after(d2)) {
            // swap dates so that d1 is start and d2 is end
            Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int days = d2.get(Calendar.DAY_OF_YEAR)
                - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                d1.add(Calendar.YEAR, 1);
            }
            while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    /**
     * 计算2个日期之间的工作天数（去除周六周日）
     *
     * @param d1 日期1
     * @param d2 日期2
     * @return 日期1和日期2之间的工作天数
     */
    public int getWorkingDay(Calendar d1, Calendar d2) {
        int result = -1;
        if (d1.after(d2)) {
            // swap dates so that d1 is start and d2 is end
            Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }

        //int betweendays = getDaysBetween(d1, d2);

        //int charge_date = 0;

        // 开始日期的日期偏移量
        int charge_start_date = 0;
        // 结束日期的日期偏移量
        int charge_end_date = 0;

        int stmp;
        int etmp;
        stmp = 7 - d1.get(Calendar.DAY_OF_WEEK);
        etmp = 7 - d2.get(Calendar.DAY_OF_WEEK);

        // 日期不在同一个日期内        
        if (stmp != 0 && stmp != 6) {// 开始日期为星期六和星期日时偏移量为0
            charge_start_date = stmp - 1;
        }
        if (etmp != 0 && etmp != 6) {// 结束日期为星期六和星期日时偏移量为0
            charge_end_date = etmp - 1;
        }
        result = (getDaysBetween(this.getNextMonday(d1), this.getNextMonday(d2)) / 7)
                * 5 + charge_start_date - charge_end_date;
        return result;
    }

    // 英文 星期
    public static final String[] WEEKS_I18N_EN = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    // 中文 星期
    public static final String[] WEEKS_I18N_CH = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    // 越南 星期
    public static final String[] WEEKS_I18N_VN = {"Chủ Nhật", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7"};
    // 泰语 星期
    public static final String[] WEEKS_I18N_TH = {"วันอาทิตย", "วันจันทร", "วันอังคาร", "วันพุธ", "วันพฤหัสบด", "วันศุกร", "วันเสาร"};

    // 中文
    public static final String COUNTRY_CHINESE = "zh";
    // 英文
    public static final String COUNTRY_ENGLISH = "en";
    // 越南
    public static final String COUNTRY_VIETNAM = "vn";
    // 泰语
    public static final String COUNTRY_THAILAND = "th";


    /**
     * 获取当前星期
     *
     * @param  date      当前日期
     * @param  lang      zh : 标识中文 , en : 标识英文（默认）, vn : 越南 , th : 泰国
     * @return String    当前星期
     * @update luoqy
     */
    public static String getWeekByDateAndLang(final Date date, String lang) throws Exception {
        String[] dayNames = null;
        if (date == null) {
            throw new Exception("获取星期的日期不能够为空");
        }
        lang = StringUtils.isNotBlank(lang) ? lang : COUNTRY_ENGLISH;
        switch (lang) {
            case COUNTRY_CHINESE :
                dayNames = WEEKS_I18N_CH;
                break;
            case COUNTRY_VIETNAM :
                dayNames = WEEKS_I18N_VN;
                break;
            case COUNTRY_THAILAND :
                dayNames = WEEKS_I18N_TH;
                break;
            case COUNTRY_ENGLISH :
            default:
                dayNames = WEEKS_I18N_EN;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayNames[dayOfWeek - 1];
    }

    /**
     * 获得日期的下一个星期一的日期
     *
     * @param date
     * @return
     */
    public Calendar getNextMonday(Calendar date) {
        Calendar result = null;
        result = date;
        do {
            result = (Calendar) result.clone();
            result.add(Calendar.DATE, 1);
        }
        while (result.get(Calendar.DAY_OF_WEEK) != 2);
        return result;
    }

    /**
     * 计算两个日期之间的非工作日天数
     *
     * @param d1 日期1
     * @param d2 日期2
     * @return 日期1与日期2之间的非工作天数
     */
    public int getHolidays(Calendar d1, Calendar d2) {
        return this.getDaysBetween(d1, d2) - this.getWorkingDay(d1, d2);

    }

	/*public static void main(String[] args) throws Exception{
		System.out.println(getPreDay(new Date()));
		System.out.println(isInDate(new Date(),str2Date("2017-06-22 10:00:00"),str2Date("2017-06-23 11:00:00")));
		System.out.println(date2Str(str2Date("2017-06-22 10:00:00"),"yyyy-MM-dd"));
		System.out.println(getInstantDishStartDateByMatchDate(new Date()));
		System.out.println(getEndMatchDate(new Date()));
	}*/

	// 以某个时间点为参考 参与计算这个时间点过去的某个时间
    public static final String DATE_DIRECTION_BEFORE = "-";
    // 以某个时间点为参考 参与计算这个时间点未来的某个时间
    public static final String DATE_DIRECTION_AFTER = "+";

    public static final String SHORT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 根据条件生成一个连续的时间段数组,可用于生成完场的日期、一周赛程的日期列表
     * @param reference 参考日期，缺省值为系统当前日期
     * @param direction 时间方向：过去、未来
     * @param intervals 时间界限
     * @return Date[]
     * @throws Exception
     * @author luoqy
     * e.g:
     *  Date[] dates = getDateArrayByCondition(null, DateUtil.DATE_DIRECTION_BEFORE,7);
     *  SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.SHORT_DATE_FORMAT);
     *  for (Date date : dates) {
     *      System.out.println(simpleDateFormat.format(date));
     *  }
     *  打印出来的结果：
     *  2017-10-12
     *  2017-10-11
     *  2017-10-10
     *  2017-10-09
     *  2017-10-08
     *  2017-10-07
     *  2017-10-06
     */
	public static Date[] getDateArrayByCondition(Date reference, final String direction,
                                          final Integer intervals) throws Exception {
        if (intervals == null) {
            throw new Exception("时间范围不能没有界限");
        }
        if (StringUtils.isBlank(direction)) {
            throw new Exception("获取时间段的方向不能为空");
        } else {
            switch (direction) {
                case DATE_DIRECTION_BEFORE :
                case DATE_DIRECTION_AFTER  :
                    break;
                default                    :
                    throw new Exception("无效的时间方向（过去、未来）");
            }
        }
        Date[] passage = new Date[intervals];
        if (reference == null) {
            reference = new Date(System.currentTimeMillis());
        }
        for (Integer i = 1; i <= intervals; i++) {
            Calendar c = Calendar.getInstance();
            c.setTime(reference);
            if (direction.equalsIgnoreCase(DATE_DIRECTION_BEFORE)) {
                c.add(Calendar.DATE, - i);
            } else if (direction.equalsIgnoreCase(DATE_DIRECTION_AFTER)) {
                c.add(Calendar.DATE, + i);
            }
            passage[i-1] = c.getTime();
        }
        return passage;
    }

	
	 /**
     * 获取竞彩比分当天
     * 
     * @return
     */
    public static String getLoteryToday() {
        DateTime natureToday = new  DateTime();
        String natureTodayStr=natureToday.toString(DateUtil.SHORT_DATE_FORMAT);
        String natureTomorrowStr=natureToday.plusDays(1).toString(DateUtil.SHORT_DATE_FORMAT);
        String lotteryToday=natureTodayStr;
        //当天11点
        DateTime today11 =DateTime.parse(natureTodayStr + " 11:00", DateUtil.YMDHM);
        DateTime tomorrow11 =DateTime.parse(natureTomorrowStr + " 11:00", DateUtil.YMDHM);
        
        long now=System.currentTimeMillis();
        if(now<today11.getMillis()){
            lotteryToday=natureToday.plusDays(-1).toString(DateUtil.SHORT_DATE_FORMAT);
        }
        if(now>=tomorrow11.getMillis()){
            lotteryToday=tomorrow11.toString(DateUtil.SHORT_DATE_FORMAT);
        }
       
        return lotteryToday;
    }
}
