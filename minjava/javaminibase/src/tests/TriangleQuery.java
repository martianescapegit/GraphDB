package tests;

import java.io.IOException;

import edgeheap.EScan;
import edgeheap.Edge;
import edgeheap.EdgeHeapfile;

import global.AttrType;
import global.EID;
import global.RID;
import global.SystemDefs;
import heap.FieldNumberOutOfBoundException;
import heap.HFBufMgrException;
import heap.HFDiskMgrException;
import heap.HFException;
import heap.Heapfile;
import heap.InvalidSlotNumberException;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import heap.Scan;
import heap.SpaceNotAvailableException;
import heap.Tuple;
import iterator.FileScan;
import iterator.JoinsException;
import iterator.LowMemException;
import iterator.SmjEdge;
import iterator.UnknowAttrType;


public class TriangleQuery {
	public String label1;
	public String label2;
	public String label3;
	
	TriangleQuery(){
		label1 = "1_2";
		label2 = "2_3";
		label3 = "3_1";
		
	}
	private Tuple setHdrFilter1(Tuple t) throws InvalidTypeException, InvalidTupleSizeException, IOException {
		AttrType[] attrs = new AttrType[6];
        short[] str_sizes = new short[1];
        attrs[0] = new AttrType(AttrType.attrString);
        attrs[1] = new AttrType(AttrType.attrInteger); //source pg no.
        attrs[2] = new AttrType(AttrType.attrInteger);//source slot no.
        attrs[3] = new AttrType(AttrType.attrInteger);//dest pg no.
        attrs[4] = new AttrType(AttrType.attrInteger);//dest slot no.
        attrs[5] = new AttrType(AttrType.attrInteger);
        str_sizes[0] = (short)44;
        t.setHdr((short)6, attrs, str_sizes);
        return t;
	}
	
	public void filterTupleLabels(EdgeHeapfile hf,  String label, 
			String outheapfile) throws HFException, HFBufMgrException, HFDiskMgrException, 
			IOException, InvalidTupleSizeException, InvalidSlotNumberException, SpaceNotAvailableException, 
			FieldNumberOutOfBoundException, edgeheap.InvalidTupleSizeException, InvalidTypeException {
		//Heapfile hf = new Heapfile(heapfilename);
		EScan fscan = new EScan(hf);
		Heapfile outhf = new Heapfile(outheapfile);
		
		
		EID rid = new EID();
        boolean done = true;
        while(done){
            Edge e  = fscan.getNext(rid);
            if(e == null){
                done = false;
                fscan.closescan();
                break;
            }
            Tuple t = new Tuple(e.getTupleByteArray(), 0, e.getLength());
            t = setHdrFilter1(t);
            if(t.getStrFld(1).equals(label)){
            	//Add to the new heapfile
//                Tuple tuple = new Tuple(t.getEdgeByteArray(),0,t.getLength());
//                tuple.setHdr((short)6, attrs, str_sizes);
                outhf.insertRecord(t.getTupleByteArray());
            }
        }
 
		
	}
	
	
	public void filterTupleLabels23(Heapfile hf,  String label, 
			String outheapfile) throws HFException, HFBufMgrException, HFDiskMgrException, 
			IOException, InvalidTupleSizeException, InvalidSlotNumberException, SpaceNotAvailableException, 
			FieldNumberOutOfBoundException, edgeheap.InvalidTupleSizeException, InvalidTypeException {
		//Heapfile hf = new Heapfile(heapfilename);
		Scan fscan = new Scan(hf);
		Heapfile outhf = new Heapfile(outheapfile);
		
		
		RID rid = new RID();
        boolean done = true;
        while(done){
            Tuple t  = fscan.getNext(rid);
            if(t == null){
                done = false;
                fscan.closescan();
                break;
            }
            t = setHdrFilter1(t);
            if(t.getStrFld(4).equals(label)){
            	//Add to the new heapfile
//                Tuple tuple = new Tuple(t.getEdgeByteArray(),0,t.getLength());
//                tuple.setHdr((short)6, attrs, str_sizes);
                outhf.insertRecord(t.getTupleByteArray());
            }
        }
 
		
	}
	
	
	public void printTuplesInRelation(String heapfilename) throws FieldNumberOutOfBoundException, IOException,
	InvalidTupleSizeException, HFException, HFBufMgrException, HFDiskMgrException, InvalidTypeException{
		Heapfile hf = new Heapfile(heapfilename);
		Scan fscan = new Scan(hf);
		RID rid = new RID();
        boolean done = true;
        while(done){
        	Tuple t  = fscan.getNext(rid);
            if(t == null){
                done = false;
                fscan.closescan();
                break;
            }
            t = setHdrFilter1(t);
            System.out.println(t.getStrFld(1));
        }
	}
	
	
	public void printTuplesInRelation2(Heapfile heapfilename) throws FieldNumberOutOfBoundException, IOException,
	InvalidTupleSizeException, HFException, HFBufMgrException, HFDiskMgrException, InvalidTypeException{
		//Heapfile hf = new Heapfile(heapfilename);
		Scan fscan = new Scan(heapfilename);
		RID rid = new RID();
        boolean done = true;
        while(done){
        	Tuple t  = fscan.getNext(rid);
            if(t == null){
                done = false;
                fscan.closescan();
                break;
            }
            t = setHdrFilter1(t);
            System.out.println(t.getStrFld(1));
        }
	}
	
	
	public void startTriangleQuery() throws UnknowAttrType, LowMemException, JoinsException, Exception{
		//From the edge relation filter label1 from outer
		EdgeHeapfile hf = SystemDefs.JavabaseDB.edgeHeapfile;

		String outheapfile = "filterlabels1";
		String inheapfile = "filterlabels2";
		filterTupleLabels(hf, label1, outheapfile);
		filterTupleLabels(hf, label2, inheapfile);
		//printTuplesInRelation(outheapfile);
		String joinheapfile = "joinheapfile";
		
		SmjEdge smj = new SmjEdge(outheapfile, inheapfile);
		printTuplesInRelation2(smj.joinHeapfile);

		//Filter inner based on label 3 
		filterTupleLabels(hf, label3, inheapfile);
		
		//Pass the already joined heapfile and the filtered file as input to smj
		
		
	}
}
