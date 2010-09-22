<div id="commandManager">
<#if model.commandManager.lastMessage?? && model.commandManager.lastMessage != ""><div id="commandMsg">${model.commandManager.lastMessage}${model.commandManager.clearLastMessage()}</div></#if>
</div>
