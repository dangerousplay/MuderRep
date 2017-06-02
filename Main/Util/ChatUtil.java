package Main.Util;

import net.minecraft.server.v1_7_R2.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class ChatUtil {

    public static IChatBaseComponent chatHover(String texto, String msgHover){
		IChatBaseComponent chatB = new ChatMessage(texto);
		chatB.setChatModifier(new ChatModifier());
		chatB.getChatModifier().a(new ChatHoverable(EnumHoverAction.SHOW_TEXT, new ChatMessage(msgHover)));
		String chatBClick = ChatSerializer.a(chatB);
		IChatBaseComponent chat = ChatSerializer.a("" +chatBClick);
		return chat;
	}

	public static IChatBaseComponent chatHover(String Inicio,String Fim,String texto, String msgHover){
		IChatBaseComponent chatB = new ChatMessage(texto);
		chatB.setChatModifier(new ChatModifier());
		chatB.getChatModifier().a(new ChatHoverable(EnumHoverAction.SHOW_TEXT, new ChatMessage(msgHover)));
		String chatBClick = ChatSerializer.a(chatB);
		IChatBaseComponent chat = ChatSerializer.a(Inicio +chatBClick + Fim);
		return chat;
	}
	
	public static IChatBaseComponent chatComando(String texto, String msgHover, String comando){
		IChatBaseComponent chatB = new ChatMessage(texto);
		chatB.setChatModifier(new ChatModifier());
		chatB.getChatModifier().setChatClickable(new ChatClickable(EnumClickAction.RUN_COMMAND, comando));
		chatB.getChatModifier().a(new ChatHoverable(EnumHoverAction.SHOW_TEXT, new ChatMessage(msgHover)));
		String chatBClick = ChatSerializer.a(chatB);
		IChatBaseComponent chat = ChatSerializer.a(chatBClick);
		return chat;
	}

	public static IChatBaseComponent ShowItem(ItemStack Item){

		List<String> Lore = new ArrayList<>();

		if(Item.hasItemMeta() && Item.getItemMeta().hasLore()){
			Lore = Item.getItemMeta().getLore();
		}

		StringBuffer SB = new StringBuffer();
		Lore.forEach(P -> SB.append(P).append(""));

		IChatBaseComponent chatB = new ChatMessage("SHOW");
		chatB.setChatModifier(new ChatModifier());
		chatB.getChatModifier().a(new ChatHoverable(EnumHoverAction.SHOW_TEXT, new ChatMessage(SB.toString())));
		String chatBClick = ChatSerializer.a(chatB);
		IChatBaseComponent chat = ChatSerializer.a(chatBClick);
		return chat;
	}
	
}