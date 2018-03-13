/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tas_sp2018;
//
/**
 *
 * @author Andrew
 */
public class Badge {
           private String description;
	   private String id;
	
	   public Badge(){}
           
           
	   public Badge(String description, String id){
		this.description = description;
		this.id = id;
	}
	
	   public String getDescription() {return description;}
	   public String getId() {return id;}


	   public void setDescription(String description) { this.description = description;}
	   public void setId(String id) { this.id = id;}


	   @Override
	   public String toString(){
		return "#" + getId() + " (" + getDescription() + ")";
	}
    }

