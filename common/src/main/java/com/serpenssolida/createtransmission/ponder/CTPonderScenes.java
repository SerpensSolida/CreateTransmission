package com.serpenssolida.createtransmission.ponder;

import com.serpenssolida.createtransmission.CTBlocks;
import com.serpenssolida.createtransmission.content.chain.AbstractTransmissionChainBlock;
import com.serpenssolida.createtransmission.content.chain.EncasedTransmissionChainBlock;
import com.serpenssolida.createtransmission.content.chain.TransmissionChainBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import static com.serpenssolida.createtransmission.content.chain.TransmissionChainHelpers.*;

public class CTPonderScenes
{
	public static void chainUsage(SceneBuilder builder, SceneBuildingUtil util)
	{
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);
		CreateSceneBuilder.WorldInstructions world = scene.world();

		scene.title("usage", "Relaying rotational force using Transmission Chains");
		scene.configureBasePlate(0, 0, 6);

		world.showSection(util.select().layer(0), Direction.DOWN);

		scene.idle(5);

		BlockPos leftChainPos = util.grid().at(3, 1, 2);
		BlockPos rightChainPos = util.grid().at(2, 1, 2);

		Selection leftChain = util.select().position(leftChainPos);
		Selection rightChain = util.select().position(rightChainPos);
		Selection leftBelt = util.select().fromTo(5, 1, 3, 3, 1 ,3);
		Selection rightBelt = util.select().fromTo(2, 1, 3, 0, 1 ,3);

		world.setKineticSpeed(rightBelt, 0);

		world.showSection(util.select().fromTo(5, 1, 3, 0, 1, 5), Direction.DOWN);
		scene.idleSeconds(2);

		scene.overlay()
			 .showText(10 + 4 * 20)
			 .placeNearTarget()
			 .text("Encased chain drive can be used to transfer rotational power between belts.");

		world.showSection(leftChain, Direction.DOWN);
		world.showSection(rightChain, Direction.DOWN);
		scene.idle(10);
		world.setKineticSpeed(rightBelt, 16);

		scene.idleSeconds(4);
		scene.idle(10);

		scene.overlay()
			 .showText(40)
			 .placeNearTarget()
			 .text("But are bulky...");

		scene.idleSeconds(2);

		world.hideSection(rightChain, Direction.DOWN);
		world.hideSection(leftChain, Direction.DOWN);
		world.setKineticSpeed(rightBelt, 0);
		world.setKineticSpeed(rightChain, 0);

		scene.idle(20);

		scene.addKeyframe();

		BlockEntry<TransmissionChainBlock> chainBlock = CTBlocks.TRANSMISSION_CHAIN;

		world.setBlocks(leftChain, chainBlock.getDefaultState()
											 .setValue(AbstractTransmissionChainBlock.FACING, Direction.SOUTH)
											 .setValue(AbstractTransmissionChainBlock.CONNECTION_SIDE, ChainSide.RIGHT)
											 .setValue(AbstractTransmissionChainBlock.CONNECTION_TYPE, ConnectionType.CHAIN), false);
		world.setBlocks(rightChain, chainBlock.getDefaultState()
											  .setValue(AbstractTransmissionChainBlock.FACING, Direction.SOUTH)
											  .setValue(AbstractTransmissionChainBlock.CONNECTION_SIDE, ChainSide.LEFT)
											  .setValue(AbstractTransmissionChainBlock.CONNECTION_TYPE, ConnectionType.CHAIN), false);

		world.showSection(leftChain, Direction.DOWN);
		scene.idle(5);
		world.setKineticSpeed(leftChain, 16);

		world.showSection(rightChain, Direction.DOWN);
		scene.idle(5);
		world.setKineticSpeed(rightChain, 16);
		scene.idle(5);
		world.setKineticSpeed(rightBelt, 16);

		scene.idle(10);

		scene.overlay()
			 .showText(4 * 20)
			 .placeNearTarget()
			 .text("As an alternative you can use Transmission Chains! They are slim and only work on belts.");

		scene.idleSeconds(5);

		scene.addKeyframe();
		scene.overlay()
			 .showText(16 * 20)
			 .placeNearTarget()
			 .text("They can power belt in different arrangements.");
		scene.idleSeconds(1);

		Vec3 textPos = new Vec3(2, 1, 4);

		scene.overlay()
			 .showText(4 * 20)
			 .placeNearTarget()
			 .text("Horizontally...")
			 .pointAt(textPos);
		scene.idleSeconds(4);


		world.hideSection(util.select().layer(1), Direction.UP);
		scene.idleSeconds(1);

		ElementLink<WorldSectionElement> showcase1 = world.showIndependentSection(util.select().layers(10, 1), Direction.DOWN);
		world.moveSection(showcase1, new Vec3(0, -9, 0), 0);

		scene.overlay()
			 .showText(4 * 20)
			 .placeNearTarget()
			 .text("...on a right angle...")
			 .pointAt(textPos);
		scene.idleSeconds(4);

		world.hideIndependentSection(showcase1, Direction.UP);
		scene.idleSeconds(1);

		ElementLink<WorldSectionElement> showcase2 = world.showIndependentSection(util.select().layers(12, 2), Direction.DOWN);
		world.moveSection(showcase2, new Vec3(0, -11, 0), 0);
		scene.overlay()
			 .showText(4 * 20)
			 .placeNearTarget()
			 .text("and vertically.")
			 .pointAt(textPos);
		scene.idleSeconds(4);

		world.hideIndependentSection(showcase2, Direction.UP);
		scene.idleSeconds(1);

		ElementLink<WorldSectionElement> showcase3 = world.showIndependentSection(util.select().layers(15, 2), Direction.DOWN);
		world.moveSection(showcase3, new Vec3(0, -14, 0), 0);
		scene.idleSeconds(4);

		scene.markAsFinished();
	}

	public static void chainEncasing(SceneBuilder builder, SceneBuildingUtil util)
	{
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);
		CreateSceneBuilder.WorldInstructions world = scene.world();

		BlockPos leftChainPos = util.grid().at(3, 1, 2);
		BlockPos rightChainPos = util.grid().at(2, 1, 2);

		BlockEntry<TransmissionChainBlock> chainBlock = CTBlocks.TRANSMISSION_CHAIN;
		BlockEntry<EncasedTransmissionChainBlock> andesiteChainBlock = CTBlocks.ANDESITE_ENCASED_TRANSMISSION_CHAIN;
		BlockEntry<EncasedTransmissionChainBlock> brassChainBlock = CTBlocks.BRASS_ENCASED_TRANSMISSION_CHAIN;

		scene.title("encasing", "Encasing Transmission Chains");
		scene.configureBasePlate(0, 0, 6);

		world.showSection(util.select().layers(0, 2), Direction.UP);

		scene.idleSeconds(1);

		for (int i = 0; i < 6; i++)
		{
			BlockPos startingPos = util.grid().at(5, 1, 3);
			encaseBelt(scene, util, startingPos.relative(Direction.Axis.X, -i), AllBlocks.ANDESITE_CASING);
			scene.idle(2);
		}

		scene.overlay()
			 .showText(8 + 9 * 20)
			 .placeNearTarget()
			 .text("Transmission Chains can also be encased!");

		scene.idleSeconds(2);
		scene.overlay().showControls(util.vector().topOf(leftChainPos), Pointing.DOWN, 20).rightClick()
			 .withItem(AllBlocks.ANDESITE_CASING.asStack());
		scene.idle(10);

		world.setBlock(leftChainPos, andesiteChainBlock.getDefaultState()
													   .setValue(AbstractTransmissionChainBlock.FACING, Direction.SOUTH)
													   .setValue(AbstractTransmissionChainBlock.CONNECTION_TYPE, ConnectionType.CHAIN)
													   .setValue(AbstractTransmissionChainBlock.CONNECTION_SIDE, ChainSide.RIGHT), true);

		scene.idle(10);

		world.setBlock(rightChainPos, andesiteChainBlock.getDefaultState()
													   .setValue(AbstractTransmissionChainBlock.FACING, Direction.SOUTH)
													   .setValue(AbstractTransmissionChainBlock.CONNECTION_TYPE, ConnectionType.CHAIN)
													   .setValue(AbstractTransmissionChainBlock.CONNECTION_SIDE, ChainSide.LEFT), true);

		scene.idleSeconds(2);

		for (int i = 0; i < 6; i++)
		{
			BlockPos startingPos = util.grid().at(5, 1, 3);
			encaseBelt(scene, util, startingPos.relative(Direction.Axis.X, -i), AllBlocks.BRASS_CASING);
			scene.idle(2);
		}

		world.setBlock(leftChainPos, chainBlock.getDefaultState()
													   .setValue(AbstractTransmissionChainBlock.FACING, Direction.SOUTH)
													   .setValue(AbstractTransmissionChainBlock.CONNECTION_TYPE, ConnectionType.CHAIN)
													   .setValue(AbstractTransmissionChainBlock.CONNECTION_SIDE, ChainSide.RIGHT), true);

		scene.idle(2);
		world.setBlock(rightChainPos, chainBlock.getDefaultState()
														.setValue(AbstractTransmissionChainBlock.FACING, Direction.SOUTH)
														.setValue(AbstractTransmissionChainBlock.CONNECTION_TYPE, ConnectionType.CHAIN)
														.setValue(AbstractTransmissionChainBlock.CONNECTION_SIDE, ChainSide.LEFT), true);

		scene.idleSeconds(2);
		scene.overlay().showControls(util.vector().topOf(leftChainPos), Pointing.DOWN, 20).rightClick()
			 .withItem(AllBlocks.BRASS_CASING.asStack());
		scene.idle(10);

		world.setBlock(leftChainPos, brassChainBlock.getDefaultState()
													   .setValue(AbstractTransmissionChainBlock.FACING, Direction.SOUTH)
													   .setValue(AbstractTransmissionChainBlock.CONNECTION_TYPE, ConnectionType.CHAIN)
													   .setValue(AbstractTransmissionChainBlock.CONNECTION_SIDE, ChainSide.RIGHT), true);

		scene.idle(10);

		world.setBlock(rightChainPos, brassChainBlock.getDefaultState()
														.setValue(AbstractTransmissionChainBlock.FACING, Direction.SOUTH)
														.setValue(AbstractTransmissionChainBlock.CONNECTION_TYPE, ConnectionType.CHAIN)
														.setValue(AbstractTransmissionChainBlock.CONNECTION_SIDE, ChainSide.LEFT), true);

		scene.idleSeconds(2);
	}

	@ExpectPlatform
	private static void encaseBelt(SceneBuilder scene, SceneBuildingUtil util, BlockPos pos, BlockEntry<CasingBlock> casing)
	{
		throw new AssertionError();
	}
}
