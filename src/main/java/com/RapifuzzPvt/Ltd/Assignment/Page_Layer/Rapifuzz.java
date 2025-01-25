package com.RapifuzzPvt.Ltd.Assignment.Page_Layer;

import java.io.IOException;

import com.RapifuzzPvt.Ltd.Assignment.Page_Or_Layer.RapifuzzOR;
import com.RapifuzzPvt.Ltd.Assignment.Util_Layer.WebUtil;

public class Rapifuzz extends RapifuzzOR {

	   WebUtil gm;

    public Rapifuzz(WebUtil gm) {
        super(gm);
        this.gm=gm;;
    }

    public void validateDestination() throws IOException {
    	
    	
    	
        gm.click(getFromINPUT(), "Click on from field");
        gm.clear(getFromINPUT());
        gm.inputValue(getFromINPUT(), "DEL"); 
        gm.clickAndReturn(getSelectDP());
       // gm.readExcelData("C:\\Users\\dell\\Desktop\\Excel Data read", "Sheet1", "Station 1");
        gm.readExcelFile("C:\\Users\\dell\\eclipse-workspace\\TDDFrameWork_By_SKG\\com.RapifuzzPvt.Ltd.Assignment\\ExcelData\\expected_stations_names.xlsx");
       // gm.getDropdownData("txtStationFrom", "C:\\Users\\dell\\eclipse-workspace\\TDDFrameWork_By_SKG\\com.RapifuzzPvt.Ltd.Assignment\\ExcelData\\Output_stations_names.xlsx");
        //gm.getdata("C:\\Users\\dell\\Desktop\\Excel Data read", "Sheet1", "Station 1");
        gm.compareExcelFiles1("C:\\Users\\dell\\eclipse-workspace\\TDDFrameWork_By_SKG\\com.RapifuzzPvt.Ltd.Assignment\\ExcelData\\expected_stations_names.xlsx", "C:\\Users\\dell\\eclipse-workspace\\TDDFrameWork_By_SKG\\com.RapifuzzPvt.Ltd.Assignment\\ExcelData\\Output_stations_names.xlsx");
        gm.click(getUserDepartureUpdate(), "click on calender"); 
        
        gm.getRondomDate(getUserAllMonth(), 1, getDateUse(), "29");
    }
    
    
    
}
