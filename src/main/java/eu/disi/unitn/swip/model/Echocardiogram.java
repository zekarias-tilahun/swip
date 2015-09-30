package eu.disi.unitn.swip.model;

import eu.disi.unitn.swip.datasource.Database;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

public class Echocardiogram{
	protected Integer A;
	protected Integer B;
	protected Integer C;
	protected Integer D;
	protected Double E;
	protected Double F;
	protected Double G;
	protected Integer H;
	protected Double I;
	protected Double J;
	protected String K;
	protected Integer L;
	protected Integer M;

	private static Pattern PATTERN = Pattern.compile(",");

	public static List<Echocardiogram> orderBy(String column){
		String sql = "SELECT * FROM echocardiogram order by " + column;
		Database.connect();
		ResultSet rs = Database.execute(sql);
		List<Echocardiogram> list = new ArrayList<>();
		try {
			while(rs.next()){
				Echocardiogram echocardiogram = new Echocardiogram();
				echocardiogram.A = rs.getInt(1);
				echocardiogram.B = rs.getInt(2);
				echocardiogram.C = rs.getInt(3);
				echocardiogram.D = rs.getInt(4);
				echocardiogram.E = rs.getDouble(5);
				echocardiogram.F = rs.getDouble(6);
				echocardiogram.G = rs.getDouble(7);
				echocardiogram.H = rs.getInt(8);
				echocardiogram.I = rs.getDouble(9);
				echocardiogram.J = rs.getDouble(10);
				echocardiogram.K = rs.getString(11);
				echocardiogram.L = rs.getInt(12);
				echocardiogram.M = rs.getInt(13);
				list.add(echocardiogram);
			}
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			Database.close();
		}
		return null;
	}

	/**
	 * For a give tuple of type {@link Echocardiogram} and an attribute list,
	 * the function fetches the data values associated with the attribute list.
	 *
	 * @param eTuple        an {@link Echocardiogram} type tuple
	 * @param attributeList list of attributes
	 * @return the values that correspond to the specified attributes from the
	 * given tuple
	 */
	public static String getTupleCorrespondingToAttributeList(Echocardiogram eTuple, String attributeList) {

		String[] attributes = PATTERN.split(attributeList);
		if (attributes != null) {
			return getDataFromFields(eTuple, attributes);
		} else {
			return getDataFromFields(eTuple, attributeList);
		}
	}

	public static String getDataFromFields(Echocardiogram eTuple, String... fieldNames){
		if(fieldNames.length == 1){
			return getDataFromField(eTuple, fieldNames[0]);
		} else {
			String data = "";
			try {
				int i = 0;
				for(String fieldName: fieldNames){
					Field field = Echocardiogram.class.getDeclaredField(fieldName);
					field.setAccessible(true);
					if(i==0){
						i++;
						data = field.get(eTuple).toString();
					} else {
						data += "," + field.get(eTuple).toString();
					}
				}
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return data;
		}
	}
	private static String getDataFromField(Echocardiogram eTuple, String fieldName){
		String data = "";
		try {
			Field field = Echocardiogram.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			data = field.get(eTuple).toString();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	public static int compareTuples(String tuple, String tuple1) {
		if (tuple.equals(tuple1))
			return 0;
		String[] values = tuple.split(",");
		String[] values1 = tuple1.split(",");
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				
				if (isNumeric(values[i])) {
					
					if (Double.parseDouble(values[i]) < Double.parseDouble(values1[i]))
						return -1;
				} else {
					if (values[i].compareTo(values1[i]) < 0){
						return -1;
					}
				}
			}
		} else {
			if (Integer.parseInt(tuple) < Integer.parseInt(tuple1))
				return -1;
		}
		return 1;
	}

	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}


	public static Map<Integer, String> columnIndexToNameMap;
	static{
		columnIndexToNameMap = new HashMap<>();
		columnIndexToNameMap.put(1, "A");
		columnIndexToNameMap.put(2, "B");
		columnIndexToNameMap.put(3, "C");
		columnIndexToNameMap.put(4, "D");
		columnIndexToNameMap.put(5, "E");
		columnIndexToNameMap.put(6, "F");
		columnIndexToNameMap.put(7, "G");
		columnIndexToNameMap.put(8, "H");
		columnIndexToNameMap.put(9, "I");
		columnIndexToNameMap.put(10, "J");
		columnIndexToNameMap.put(11, "K");
		columnIndexToNameMap.put(12, "L");
		columnIndexToNameMap.put(13, "M");
	}
}
