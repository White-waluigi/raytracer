package render;

import java.util.ArrayList;
import java.util.List;

import ray.Vec3;
import render.BSVObject.BoundingSphere;
import render.Scene.Combo;

//This is used for Optimisation
public class BSVNode implements BSVObject{

	
	//A BSVObject is either a Render Object or a BSV Node
	public List<BSVObject> children=null;
	Vec3 color;
	
	private BSVNode() {
		children=new ArrayList<BSVObject>();
		//Color can be used to identfy Pairs
		color=new  Vec3(Math.random(),Math.random(),Math.random());

	}

	//Ever BSV Node has 2 Nodes or 2 or 1 RenderObjects as children
	public void add(BSVObject o) {

		
		if(o!=null)
			children.add(o);
		
	}

	//Make Tree from List
	public static BSVNode makeTree(List<RenderObject> allObjs) {
		
		//Create a List of BSVObject from List of RenderObjects
		List<BSVObject> leaves=new ArrayList<BSVObject>();
		for(RenderObject x:allObjs) {
			leaves.add(x);
		}

		//Create a binary tree from all the remaining Nodes and RenderObjects
		while(leaves.size()!=1) {
			List<BSVObject> temp=new ArrayList<BSVObject>();

			
			//Work through every level of the tree
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
		BSVObject x=leaves.remove(0);
		
		
		//If we only have a single object, add it to a Node, so a Node can be returned
		if(!(x instanceof BSVNode)) {
			BSVNode n=new BSVNode();
			n.add(x);
			return n;
			
			
		}
		return (BSVNode) x;
		
		
	}

	//Get Bounding Sphere from Childrens Bounding Sphere
	@Override
	public BoundingSphere getBoundingSphere() {
		
		//return new BoundingSphere(new Vec3(0),10e12f);
		if(children.size()==1)
			return children.get(0).getBoundingSphere();
		
			
		return children.get(0).getBoundingSphere().merge(children.get(1).getBoundingSphere());
		
	}
	
	public String toString() {
		return toString(0);
	}
	//Get a reqursive tree String
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

	
	//Check Intersection recusively
	public Combo intersect(Ray ray) {
		
		
		//Return empty if no collision
		if(!getBoundingSphere().collide(ray)) {
			return new Combo();
		}
		
		
		//Otherwise check children for collision
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
				//Heres the recursion
				Combo tc=((BSVNode)ob).intersect(ray);
				ret=tc.ray;
				o=tc.r;
				
			}
			//Update to the shortest intersection
			if (ret != null && shortest.subtract(ray.pos).length()>ret.pos.subtract(ray.pos).length()) {
				shortest=ret.pos;
				retv.ray=ret;
				retv.r=o;
			}
			
			
		}
		return retv;

	}
	
}
