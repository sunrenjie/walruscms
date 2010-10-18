<div id="commandManager">
<div id="commandManagerButtons">
<#if model.commandManager.canUndo()>
	<a href="#" onclick="undo(); return false;" title="${model.commandManager.undoTitle}"><img src="../img/undo_btn.gif" /></a>
<#else/>
	<img src="../img/undo_btn_passive.gif" />
</#if> 
 
<#if model.commandManager.canRedo()>
	<a href="#" onclick="redo(); return false;" title="${model.commandManager.redoTitle}"><img src="../img/redo_btn.gif" /></a>
<#else/>
	<img src="../img/redo_btn_passive.gif" />
</#if>
</div>
<#if model.commandManager.lastMessage?? && model.commandManager.lastMessage != ""><div id="commandMsg">${model.commandManager.lastMessage}${model.commandManager.clearLastMessage()}</div></#if>
</div>
