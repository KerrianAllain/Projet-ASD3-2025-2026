# ASD3 Project - ALLAIN Kerrian & ALLAIN Ethann

## Non Interactive Mode

```shell
java -classpath AllainAllain/bin Main {imagePath} {CompressionMethod} {parameter}
```

`CompressionMethod` is either `Lambda` or `Phi`

## Interactive Mode

```shell
java -classpath AllainAllain/bin Main
```

The image to work with will be asked to begin the operations \
Then the operations using a RQuadtree or an AVL can be typed in the terminal \
`avlAction` is either `search`, `add` or `delete`

## Generated Images & Texts

For the Non Interactive Mode and the Interactive Mode the generated images and textuals representations are saved in the Target directory in a directory corresponding to the image and operation done

## Compare two images

To compare two images you must use the Interactive Mode and use the command compare and enter the files names
