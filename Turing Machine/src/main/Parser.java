package main;
import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import simulation.State;
import simulation.TransitionFunction;


public class Parser {
	
	// Variables
	private String alphabet;
	private ArrayList<State> allStates;
	private State startState;
	private ArrayList<State> acceptStates;
	private ArrayList<State> rejectStates;
	private ArrayList<TransitionFunction> transitionFunction;
	private String tape;
	private String startPosition;


	public String parseAndValidate(String text) throws JsonMappingException, JsonProcessingException {
	// TODO Check if blank symbol is in alphabet if it does throw error 
		
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode an;
		Iterator<JsonNode> iterator;
		JsonNode node;
		State state;
		String s_State = null;
		JsonNode root = null;
		String erg = "INVALID: ";


		if(text.isBlank())
		{
			return "EMPTY";
		}
		
		try 
		{
			root = mapper.readTree(text);
		}
		catch (Exception e) {
			
			return "INVALID JSON";
		}
		
		
		try
		{
			if(root.get("alphabet").asText().equals(" "))
			{
				return "BLANK: ALPHABET";
			}
			
			setAlphabet(root.get("alphabet").asText());	
		}
		catch(Exception e) {
			
			System.out.println("TEMPLATE: alphabet");
			return "TEMPLATE: alphabet";
		}
		
		
		try {
			
			setTape(root.get("tape").asText());
		}
		catch (Exception e) {
			
			System.out.println("Template Error: tape");
		}
		
		
		try
		{
			setAllStates(new ArrayList<State>());
			an =  (ArrayNode) root.get("allStates");
			
			iterator = an.elements();
			
			while(iterator.hasNext())
			{
				node = iterator.next();
				state = new State(node.asText());
				if(!(state.toString().equals(" ") || state.toString().equals("")))
				{
					allStates.add(state);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Template Error: allStates");
		}
		
		
		try {
			s_State = root.get("startState").asText();
			
			for(State s: getAllStates() )
			{
				/*System.out.println(s);
				System.out.println(getStartState());*/
				
				if(s.toString().equals(s_State))
				{
					System.out.println("VALID StartState");
					setStartState(new State(s_State));
					break;
				}
				else
				{
					erg = "INVALID StartState";
					return erg;
				}
			}
		}
		catch(Exception e )
		{
			System.out.println("Template Error: startState");
		}
		
		
		try {
			setStartPosition(root.get("startPosition").asText());
		}
		catch (Exception e) 
		{
			System.out.println("Template Error: startPosition");
		}

		
		try {
			setRejectStates(new ArrayList<State>());
			
			an =  (ArrayNode) root.get("rejectStates");
			iterator = an.elements();
			
			while(iterator.hasNext())
			{
				node = iterator.next();
				state = new State(node.asText());
				
				
				for(State s: getAllStates() )
				{
										
					if(s.toString().equals(state.toString()))
					{
						System.out.println("VALID RejectState");
						rejectStates.add(state);
						
					}
				}
			}
			erg = "INVALID RejectState";
		}
		
		catch (Exception e) {
			System.out.println("Template Error: rejectStates");
		}
		
		
		try {
			
			setAcceptStates(new ArrayList<State>());
			an =  (ArrayNode) root.get("acceptStates");
			iterator = an.elements();
			
			while(iterator.hasNext())
			{
				node = iterator.next();
				state = new State(node.asText());
				
					if(getAllStates().contains(state))
					{
						System.out.println("VALID AcceptState");
						acceptStates.add(state);
					}
										
					/*if(s.toString().equals(state.toString()))
					{
						System.out.println("VALID AcceptState");
						acceptStates.add(state);
					}
					else
					{
						erg = "INVALID AcceptState";
						return erg;
					}*/
				}
		}
		catch (Exception e) {
			System.out.println("Template Error: acceptStates");
		}
		
		try {
			setTransitionFunction(new ArrayList<TransitionFunction>());
			
			TransitionFunction tf;
			an = (ArrayNode) root.get("transitionFunction");
			iterator = an.elements();
			
			
			while(iterator.hasNext())
			{
				node = iterator.next();
				
				tf = new TransitionFunction(new State(node.get("previousState").asText()),  
											node.get("readSymbol").asText().charAt(0),
											new State(node.get("newState").asText()),
											node.get("writtenSymbol").asText().charAt(0),
											node.get("movement").asText().charAt(0));
				
				this.transitionFunction.add(tf);
					
			}
		}
		catch (Exception e) {
			System.out.println("Template Error: transitionFunction");
		}
				
		
		return erg;
	}
	
	@Override
	public String toString(){
		String out = "\n";
		out += " alphabet: " +  getAlphabet() + "\n";
		out += " tape: " +  getTape() + "\n";
		out += " startState:" +  getStartState() + "\n";
		out += " startPosition: " +  getStartPosition() + "\n";
		out += " allStates: " + getAllStates() + "\n";
		out += " rejectStates: " +  getRejectStates() + "\n";
		out += " acceptStates: " +  getAcceptStates() + "\n";
		out += " transitionFunction:" +  getTransitionFunction();
		
		return out;
	}
	
	public String getAlphabet() {
		return alphabet;
	}

	public void setAlphabet(String alphabet) {
		this.alphabet = alphabet;
	}

	public ArrayList<State> getAllStates() {
		return allStates;
	}

	public void setAllStates(ArrayList<State> states) {
		this.allStates = states;
	}

	public State getStartState() {
		return startState;
	}

	public void setStartState(State startState) {
		this.startState = startState;
	}

	public ArrayList<State> getAcceptStates() {
		return acceptStates;
	}

	public void setAcceptStates(ArrayList<State> acceptStates) {
		this.acceptStates = acceptStates;
	}

	public ArrayList<State> getRejectStates() {
		return rejectStates;
	}

	public void setRejectStates(ArrayList<State> rejectStates) {
		this.rejectStates = rejectStates;
	}

	public ArrayList<TransitionFunction> getTransitionFunction() {
		return transitionFunction;
	}
	public void setTransitionFunction(ArrayList<TransitionFunction> transitionFunction) {
		this.transitionFunction = transitionFunction;
	}
	public String getTape() {
		return tape;
	}

	public void setTape(String tape) {
		this.tape = tape;
	}
	public String getStartPosition() {
		return startPosition;
	}
	public void setStartPosition(String startPosition) {
		this.startPosition = startPosition;
	}

}