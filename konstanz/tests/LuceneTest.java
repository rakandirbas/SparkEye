package de.uni.konstanz.tests;

import java.io.IOException;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class LuceneTest {
	public static void main(String[] args) {
		Directory dir = new RAMDirectory();
		WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_43);
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_43, analyzer);
		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		iwc.setRAMBufferSizeMB(100);
		try {
			IndexWriter writer = new IndexWriter(dir, iwc);
			Document doc = new Document();
			String txt = null;
			doc.add( new StringField( "ID", "123", Field.Store.NO ) );
			doc.add( new StringField( "Tag", "Tag1", Field.Store.YES ) );
			doc.add( new StringField( "Tag", "Tag2", Field.Store.YES ) );
			doc.add( new StringField( "Tag", "Tag3", Field.Store.YES ) );
			doc.add( new StringField( "NULL", txt, Field.Store.YES ) );
			writer.addDocument(doc);
			writer.close();
			
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(reader);
			Term t = new Term("Tag", "Tag1");
			Query q = new TermQuery( t );
			TopDocs topDocs = searcher.search(q, 5);
			ScoreDoc[] hits = topDocs.scoreDocs;
			
			for( ScoreDoc hit : hits ) {
				Document sDoc = searcher.doc(hit.doc);
				for (String s : sDoc.getValues("Tag") ) {
					System.out.println(s);
				}
				//System.out.println( "Printing the null: " + sDoc.get("NULL") );
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


















