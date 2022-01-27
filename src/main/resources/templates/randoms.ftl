<html>
    <body>
        <h3>10 Random Values</h3>
        <table border="1">
            <tr>
                <th>#</th><th>Random Value</th>
            </tr>
            <#assign count = 1>
            <#list randomValues as value>
                <tr>
                    <td>${count}</td><td>${value}</td>
                </tr>
                <#assign count = count + 1>
            </#list>
        </table>
        <p><a href="?cmd=home">Click here</a> to see the home page!</p>
    </body>
</html>
