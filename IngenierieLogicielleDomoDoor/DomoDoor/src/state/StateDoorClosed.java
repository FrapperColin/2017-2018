package state;

public class StateDoorClosed implements StateDoor{


	@Override
	public void opening() {
		System.out.println("Ouverture de la porte ....");
	}

	@Override
	public void closing() {
		System.out.println("Impossible car la porte est déja fermée");
		
	}

	@Override
	public void pause() {
		System.out.println("Impossible car la porte est déja fermée");
	}

	@Override
	public void closed() {
		System.out.println("Impossible car la porte est déja fermée");		
	}

	@Override
	public void opened() {
		System.out.println("Impossible car la porte est déja fermée");		
	}

	@Override
	public void blocked() {
		System.out.println("Impossible car la porte est déja fermée");		
	}

	@Override
	public void recovery() {
		System.out.println("Impossible car la porte est déja fermée");				
	}
	
	@Override
	public void getState() {
		System.out.println("Etat porte fermée");
		
	}

}
