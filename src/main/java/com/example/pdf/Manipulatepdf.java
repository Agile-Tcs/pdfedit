package com.example.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.PDEncryption;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

public class Manipulatepdf {
	private static final long serialVersionUID = 3149151943127976520L;
	private static final Logger logger = LogManager.getLogger(Manipulatepdf.class);
	private final static ByteArrayOutputStream os = new ByteArrayOutputStream();	
	public static final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
	
	/**
	 * Constructor
	 */
	public static void main(String args[]) {
		// Start with our document template
		try {
			String fileName = "src\\main\\resources\\0032a_orig.pdf";
			String newFileName = "src\\main\\resources\\0032a_updated.pdf";
			//String fileName = "src\\main\\resources\\0032a_updated.pdf";
			File myFile = new File(fileName);
			PDDocument pdDoc = PDDocument.load(myFile);
			AccessPermission accessPermission =pdDoc.getCurrentAccessPermission();
	        PDEncryption encryption = pdDoc.getEncryption();
			/*
			 * if (pdDoc.isEncrypted()) { try { pdDoc.setAllSecurityToBeRemoved(true); }
			 * catch (Exception e) { throw new
			 * Exception("The document is encrypted, and we can't decrypt it.", e); } }
			 */
	        PDDocumentCatalog pdCatalog = pdDoc.getDocumentCatalog();
	        PDAcroForm acroForm = pdCatalog.getAcroForm();
	
	        if (acroForm == null) {
	            System.out.println("No form-field --> stop");
	            return;
	        }
	        acroForm.setAppendOnly(true);
	        
	        List<PDField> fields = acroForm.getFields();
	        //printFields(acroForm);
	        for (PDField field : fields) {
	        	//System.out.println(field.getFullyQualifiedName());
				if (field.getFullyQualifiedName().equals("First Name")) {
					field.setValue("Manas");
				  //field.setReadOnly(false);
				}
				if (field.getFullyQualifiedName().equals("Last Name")) {
					  field.setValue("Sahu");
					  //field.setReadOnly(false);
				}
	        }		
	        
	        accessPermission.setCanModify(true);
	        accessPermission.setCanFillInForm(true);
	        StandardProtectionPolicy standardProtectionPolicy = new
	        StandardProtectionPolicy("", "", accessPermission);
	        acroForm.refreshAppearances();
	   		pdDoc.protect(standardProtectionPolicy);
	   		pdDoc.setEncryptionDictionary(encryption);
	        pdDoc.save(newFileName);
	        pdDoc.close();
        } catch (Exception e2) {
			logger.error("Error while creating the updated pdf file.", e2);
		}
	}

	private static void printFields(PDAcroForm acroForm) {
		List fields = null;
		fields = acroForm.getFields();
		Iterator fieldsItr = fields.iterator();

		while(fieldsItr.hasNext()) {
			PDField field = (PDField)fieldsItr.next();
			System.out.println(field.getPartialName());
		}
	}
}