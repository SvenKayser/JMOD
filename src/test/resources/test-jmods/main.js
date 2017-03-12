
Settings.showToolHarvestLevels(true);

var toolmat = [
    ["WOOD",            0,   88,  2.1,  0.3,   2, "plankWood",      null],
    ["STONE",           2,  601,  3.9,  0.9,   5, "cobblestone",    null], // Assuming Feldspar
    ["FLINT",           3,  712,  4.0,  0.5,   3, "itemFlint",      null],
    ["GOLD",            4,  154,  7.1,  7.2,  38, "ingotGold",      null],
    ["IRON",            5,  235,  5.4,  1.9,  10, "ingotIron",      null],
    ["EMERALD",         8, 1674,  5.8,  1.9,   7, "gemDiamond",     null]
];

for (var m in toolmat) {
    var material = toolmat[m];
    if ((material[7] == null) || isModLoaded(material[7]))
        addToolMaterial(material[0],material[1],material[2],material[3],material[4],material[5],material[6]);
}
