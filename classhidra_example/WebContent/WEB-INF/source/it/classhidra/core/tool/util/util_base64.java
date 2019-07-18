package it.classhidra.core.tool.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//import javax.xml.bind.DatatypeConverter;

public class util_base64 {
	
	public static void main(String[] args) {

		System.out.print("Enter value: ");
		String value = readInput();
		try{
	        String hash = encode(value.getBytes());
			System.out.println(hash);

		}catch(Exception e){
			
		}
	}
	
	   public static String readInput () {

		      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		      String value = null;
		      try {
		         value = br.readLine();
		      } catch (IOException ioe) {
		         System.out.println("IO error trying to read your name!");
		         System.exit(1);
		      }

		      return value;

		   }
	
	
	public static String encode( byte[] val ) {
//		return DatatypeConverter.printBase64Binary(val);
//		return Base64.getEncoder().encodeToString(val);
//		return encodeAsString(val);
		return encodeImpl(val);
	}
	public static byte[] decode( String base64string ) {
//		return DatatypeConverter.parseBase64Binary(base64string);
//		return Base64.getDecoder().decode(base64string);
//		return decodeAsByteArray(base64string);
		return decodeImpl(base64string);
	}
	
    private final static char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    private static int[]  toInt   = new int[128];

    static {
        for(int i=0; i< ALPHABET.length; i++){
            toInt[ALPHABET[i]]= i;
        }
    }

    /**
     * Translates the specified byte array into Base64 string.
     *
     * @param buf the byte array (not null)
     * @return the translated Base64 string (not null)
     */
    public static String encodeImpl(byte[] buf){
        int size = buf.length;
        char[] ar = new char[((size + 2) / 3) * 4];
        int a = 0;
        int i=0;
        while(i < size){
            byte b0 = buf[i++];
            byte b1 = (i < size) ? buf[i++] : 0;
            byte b2 = (i < size) ? buf[i++] : 0;

            int mask = 0x3F;
            ar[a++] = ALPHABET[(b0 >> 2) & mask];
            ar[a++] = ALPHABET[((b0 << 4) | ((b1 & 0xFF) >> 4)) & mask];
            ar[a++] = ALPHABET[((b1 << 2) | ((b2 & 0xFF) >> 6)) & mask];
            ar[a++] = ALPHABET[b2 & mask];
        }
        switch(size % 3){
            case 1: ar[--a]  = '=';
            case 2: ar[--a]  = '=';
        }
        return new String(ar);
    }

    /**
     * Translates the specified Base64 string into a byte array.
     *
     * @param s the Base64 string (not null)
     * @return the byte array (not null)
     */
    public static byte[] decodeImpl(String s){
        int delta = s.endsWith( "==" ) ? 2 : s.endsWith( "=" ) ? 1 : 0;
        byte[] buffer = new byte[s.length()*3/4 - delta];
        int mask = 0xFF;
        int index = 0;
        for(int i=0; i< s.length(); i+=4){
            int c0 = toInt[s.charAt( i )];
            int c1 = toInt[s.charAt( i + 1)];
            buffer[index++]= (byte)(((c0 << 2) | (c1 >> 4)) & mask);
            if(index >= buffer.length){
                return buffer;
            }
            int c2 = toInt[s.charAt( i + 2)];
            buffer[index++]= (byte)(((c1 << 4) | (c2 >> 2)) & mask);
            if(index >= buffer.length){
                return buffer;
            }
            int c3 = toInt[s.charAt( i + 3 )];
            buffer[index++]= (byte)(((c2 << 6) | c3) & mask);
        }
        return buffer;
    } 	
/*	
	public static String decodeAsString(String b64string){
        return new String(decodeAsByteArray(b64string));
    }

    public static byte[] decodeAsByteArray(String b64string){
    	try {
	        InputStream in = MimeUtility.decode(new ByteArrayInputStream(
	                         b64string.getBytes()), "base64");
	
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        
	        while(true){
	            int b = in.read();
	            if (b == -1) break;
	            else out.write(b);
	        }
	    
	        return out.toByteArray();
    	}catch (Exception e) {
			return null;
		}
    }

    public static String encodeAsString(String plaintext){
        return encodeAsString(plaintext.getBytes());
    }

    public static String encodeAsString(byte[] plaindata){
    	try {
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        ByteArrayOutputStream inStream = new ByteArrayOutputStream();
	
	        inStream.write(plaindata, 0, plaindata.length);
	
	        // pad
	        if ((plaindata.length % 3 ) == 1)
	        {
	            inStream.write(0);
	            inStream.write(0);
	        }
	        else if((plaindata.length % 3 ) == 2)
	        {
	            inStream.write(0);
	        }
	
	        inStream.writeTo(MimeUtility.encode(out, "base64"));
	        return out.toString();
    	}catch (Exception e) {
			return null;
		}
    }	
*/    
}
