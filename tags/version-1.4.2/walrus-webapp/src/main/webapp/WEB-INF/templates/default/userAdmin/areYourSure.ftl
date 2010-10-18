<div class="administrationPage">

<h1>Vartotojo trynimas</h1>
        <p>Ar tikrai norite pašalinti vartotoją <strong>${user.firstName!""} ${user.lastName!""} (${user.email!""})</strong>?</p>
        <form action="" method="POST">
                        <div class="buttonGroup">
                        		<input type="submit" name="_eventId_ok" value="Taip"/>
                        		<input type="submit" name="_eventId_cancel" value="Ne"/>
                        </div>
        </h:form>
</div>