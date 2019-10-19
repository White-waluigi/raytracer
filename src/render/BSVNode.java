package render;

import java.util.ArrayList;
import java.util.List;

import ray.Vec3;
import render.Scene.Combo;


public class BSVNode implements BSVObject{

	
	public List<BSVObject> children=null;
	Vec3 color;
	
	private BSVNode() {
		children=new ArrayList<BSVObject>();
		color=new  Vec3(Math.random(),Math.random(),Math.random());

	}

	public void add(BSVObject o) {

		
		if(o!=null)
			children.add(o);
		
	}

	public static BSVNode makeTree(List<RenderObject> allObjs) {
		
		
		List<BSVObject> leaves=new ArrayList<BSVObject>();
		for(RenderObject x:allObjs) {
			leaves.add(x);
		}
//		
//		while(!allObjs.isEmpty()) {
//			RenderObject first=allObjs.remove(0);
//			float min=Float.MAX_VALUE;
//			RenderObject partner=null;
//			
//			for(RenderObject x:allObjs) {
//				float dist=first.getBounding().distance(x.getBounding());
//				if(min>dist) {
//					min=dist;
//					partner=x;
//				}
//				
//				BSVNode l = new BSVNode();
//				l.add(first);
//				l.add(partner);
//				
//				leaves.add(l);
//				
//			}
//		}
		
		while(leaves.size()!=1) {
			List<BSVObject> temp=new ArrayList<BSVObject>();

			while(!leaves.isEmpty()) {
				BSVObject first=leaves.remove(0);
				float min=Float.MAX_VALUE;
				BSVObject partner=null;
				
				for(BSVObject x:leaves) {
					float dist=first.getBoundingSphere().distance(x.getBoundingSphere());
					
					if(min>dist) {
						min=dist;
						partner=x;
					}
					
					
				}
				if(partner!=null) {
					BSVNode l = new BSVNode();
					l.add(first);
					l.add(partner);
					leaves.remove(partner);
					
					
					temp.add(l);
				}else {
					temp.add(first);
				}
			}
			leaves.addAll(temp);
			
		}
		return (BSVNode) leaves.remove(0);
		
		
	}

	@Override
	public BoundingSphere getBoundingSphere() {
		if(children.size()!=2)
			throw new RuntimeException();
		
		return children.get(0).getBoundingSphere().merge(children.get(1).getBoundingSphere());
		
	}
	
	public String toString() {
		return toString(0);
	}
	public String toString(int i) {
		String s="";
		int k=i;
		String ind="";
		while(k--!=0) {
			ind+=" ";
		}
		s+="\n";
		s+=ind+this.hashCode()+"\n";
		if(children.isEmpty())
			throw new RuntimeException();
			
		for(BSVObject n:children) {
			String g=" "+ind+n+"\n";
			
			if(n instanceof BSVNode) {
				g=((BSVNode)n).toString(i+1);
			}
			s+=g;
			
		}

		return s;
	}

	public Combo intersect(Ray ray) {
		
		if(getBoundingSphere().intersect(ray)==null) {
			return new Combo();
		}
		
		Combo retv = new Combo();
		Vec3 shortest=new Vec3(Float.MAX_VALUE);
		Ray c=new Ray(shortest, shortest);
		retv.ray=c;
		for(BSVObject ob:children) {
			RenderObject o;
			Ray ret=null;
			if(ob instanceof RenderObject) {
				o=((RenderObject)ob);
				
				ret=o.intersect(ray);

			}else {
				Combo tc=((BSVNode)ob).intersect(ray);
				ret=tc.ray;
				o=tc.r;
				
			}
			if (ret != null && shortest.subtract(ray.pos).length()>ret.pos.subtract(ray.pos).length()) {

				shortest=ret.pos;
				retv.ray=ret;
				retv.r=o;
				
				
			}
			
			
		}
		return retv;

	}
	
}
