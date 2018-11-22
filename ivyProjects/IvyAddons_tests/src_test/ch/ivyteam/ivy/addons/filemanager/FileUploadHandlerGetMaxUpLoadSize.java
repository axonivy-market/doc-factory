/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import org.junit.Test;

/**
 * @author ec
 *
 */
public class FileUploadHandlerGetMaxUpLoadSize {
	
	private String getMaxUploadSize(int maxSize) {
		String s;
		if(maxSize>=1024) {
			float f = (float) maxSize;
			if(f%1024>0) {
				Double d = new Double(f/1024);
				s=new DecimalFormat("0.00").format(d);
			}else{
				double i = f/1024.0;
				s=""+i;
			}
			s+=" Mb";
		}else{
			s=""+maxSize+" Kb";
		}
		return s;
	}
	
	@Test
	public void getForKb(){
		assertEquals(this.getMaxUploadSize(250), "250 Kb");
	}
	
	@Test
	public void getForMb(){
		System.out.println(this.getMaxUploadSize(2048));
		assertEquals(this.getMaxUploadSize(2048), "2.0 Mb");
	}
	
	@Test
	public void getForMbAndComma(){
		System.out.println(this.getMaxUploadSize(25600));
		assertEquals(this.getMaxUploadSize(25600), "25.0 Mb");
	}
	
	@Test
	public void getForMbAndComma2(){
		System.out.println(this.getMaxUploadSize(456456));
		assertEquals(this.getMaxUploadSize(456456), "445,76 Mb");
	}

}
