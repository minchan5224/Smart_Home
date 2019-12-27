<?php

    echo "<br>";
    $value = $_POST['value'];
    $choice = $_POST['choice'];
    $select = $_POST['select'];
    $filename = "/storage/ssd1/547/11465547/public_html/select.txt";
    $fw_1 = fopen($filename, "w+");
    fwrite ($fw_1, $select);
    fclose($fw_1);
   
    $index = 0;
    
    if($choice == "1"){
        echo "<br>";
        $filename = "/storage/ssd1/547/11465547/public_html/gas.txt";
        $fw = fopen($filename, "w+");
        fwrite ($fw, $value);
        fclose($fw);
        echo "<script>location.replace('/gas_read.php')</script>";
    }
    if($choice=="2") {
        echo "<br>";
        $filename = "/storage/ssd1/547/11465547/public_html/camera.txt";
        $fw = fopen($filename, "w+");
        fwrite ($fw, $value);
        fclose($fw);
        echo "<script>location.replace('/camera_read.php')</script>";
    }
?>




