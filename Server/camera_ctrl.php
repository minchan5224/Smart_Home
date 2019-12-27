<?php

    echo "<br>";
    $value = $_POST['value'];
    $filename = "/storage/ssd1/547/11465547/public_html/camera.txt";
    $fw = fopen($filename, "w+");
    fwrite ($fw, $value);
    fclose($fw);
?>




