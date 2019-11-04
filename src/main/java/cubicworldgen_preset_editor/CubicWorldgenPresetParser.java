package cubicworldgen_preset_editor;

import java.util.function.Supplier;

import foghrye4.swing.tree.JsonSerializableTreeNode;
import vertical_spawn_control_client.minecraft.PresetParser;
import vertical_spawn_control_client.minecraft.SpawnLayer;
import vertical_spawn_control_client.ui.MainWindow;

public class CubicWorldgenPresetParser extends PresetParser {

	public CubicWorldgenPresetParser(MainWindow ownerIn) {
		super(ownerIn, new  Supplier<JsonSerializableTreeNode>() {

			@Override
			public SpawnLayer get() {
				return new SpawnLayer(ownerIn.parser);
			}
			
		});
	}

}
