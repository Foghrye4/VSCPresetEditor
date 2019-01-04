package vertical_spawn_control_client.minecraft.ai;

public enum AIModificatorAction {
	ADD, REMOVE;

	public static String[] toArrayOfString() {
		return new String[] {ADD.toString().toLowerCase(),REMOVE.toString().toLowerCase()};
	}
}
