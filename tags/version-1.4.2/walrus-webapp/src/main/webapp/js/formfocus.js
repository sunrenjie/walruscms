var bUsernameHasFocus = false;
                        var bPasswordHasFocus = false;
               
                        function setFocus()
                        {
                                // Set focus to username
                                if (bUsernameHasFocus == false && bPasswordHasFocus == false)
                                {
                                        document.getElementById("username").focus();
                                }
                        }