package com.micromouselab.mazes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MazeMapperTests {

    private final String base64digest = "sKCA4NDQkKDg0LCAoICAwJDAMMBQUDCAwFDQcLBgUHBwMKAgIECwYDAAIKDAkEDQkODQ0LAgwNCwAKDgMGAwQBCAIECQoEAwwFDQkMCQoEBQMOAQIMAwgABgEEBQcJBAcNDQUNBwkEAQgGBwUNBwULAgQBAg4FBQUFCwoCBg0FCQ4FAwoOBQUHAwoOCwoEBwMMAQ4JDgcFCwoKDA0LAAwJAgIKAgwLBA0LCAYBCAQHBQsICA4BCAIGCQQJBgUFDQULBgUNBQMODQUFAQ4FAQYBCAoGBQEKDgUFAwQLBAMOBwUJCAQBDg0FBQ0BDgULDAsGBwcDAgoCAgIGBwsCCgYA==";
    private final String hexDigest = "B0A080E0D0D090A0E0D0B080A08080C090C030C050503080C050D070B06050707030A0202040B060300020A0C09040D090E0D0D0B020C0D0B000A0E0306030401080204090A04030C050D090C090A0405030E01020C03080006010405070904070D0D050D07090401080607050D07050B020401020E050505050B0A02060D05090E05030A0E050507030A0E0B0A0407030C010E090E07050B0A0A0C0D0B000C0902020A020C0B040D0B080601080407050B08080E010802060904090605050D050B06050D05030E0D0505010E05010601080A0605010A0E050503040B04030E0705090804010E0D05050D010E050B0C0B06070703020A02020206070B020A060";
    private final String asciiGrid =
            "+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n" +
            "|               |   |   |           |   |                       |\n" +
            "+---+---+   +---+   +   +   +---+---+   +---+   +---+   +   +   +\n" +
            "|       |       |   |   |           |   |   |   |       |   |   |\n" +
            "+   +   +---+   +   +   +---+   +   +   +   +---+---+---+   +---+\n" +
            "|   |                   |       |                   |       |   |\n" +
            "+---+---+---+---+---+   +---+---+---+   +---+---+   +   +   +   +\n" +
            "|       |   |   |           |   |               |       |       |\n" +
            "+   +---+   +   +---+---+   +   +---+   +---+---+---+---+---+   +\n" +
            "|               |           |       |   |   |       |           |\n" +
            "+   +   +---+   +   +---+   +---+   +   +   +   +   +   +---+   +\n" +
            "|   |       |           |               |       |   |   |       |\n" +
            "+   +---+---+   +---+   +---+   +   +---+   +   +   +---+   +   +\n" +
            "|   |   |   |   |   |   |       |           |   |   |   |   |   |\n" +
            "+---+   +   +   +   +---+   +   +   +   +---+---+   +   +---+   +\n" +
            "|           |           |   |   |   |   |               |   |   |\n" +
            "+---+---+   +   +---+---+   +   +   +   +---+---+---+---+   +   +\n" +
            "|       |   |           |   |   |   |           |           |   |\n" +
            "+   +---+   +---+---+---+   +   +---+---+---+---+---+---+   +---+\n" +
            "|       |       |       |   |   |               |   |           |\n" +
            "+---+   +   +---+   +---+---+   +---+---+---+   +   +---+   +   +\n" +
            "|                       |       |   |           |           |   |\n" +
            "+   +---+---+---+---+   +---+   +   +---+   +---+   +   +   +---+\n" +
            "|   |               |               |       |       |   |   |   |\n" +
            "+   +---+   +   +---+   +   +---+---+   +   +   +---+   +   +   +\n" +
            "|   |       |   |   |   |       |   |   |   |       |   |       |\n" +
            "+   +---+---+   +   +   +---+---+   +   +   +   +---+   +   +---+\n" +
            "|               |   |           |   |   |       |       |       |\n" +
            "+   +   +---+---+   +   +---+---+   +   +---+   +---+   +---+---+\n" +
            "|   |   |           |       |   |   |   |   |       |   |       |\n" +
            "+---+   +   +   +   +   +---+   +   +   +   +   +---+   +---+   +\n" +
            "|       |   |   |                           |   |               |\n" +
            "+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n";


    private final Long id = 5L;
    private final String description = "This is a description!";


    @Test
    void testMapToDTO(){

        MazeEntity mazeEntity = new MazeEntity(base64digest, description);

        MazeDTO mazeDTO = MazeMapper.mapToDTO(mazeEntity);
        assertEquals(mazeEntity.getId(), mazeDTO.id());
        assertEquals(mazeEntity.getDescription(), mazeDTO.description());
        assertEquals(mazeEntity.getRawRepresentation(), mazeDTO.representation());
        assertEquals(MazeFormat.B64_DIGEST, mazeDTO.mazeFormat());
    }

    @Test
    void testMapToDTOMapsToBase64WhenSpecified(){
        MazeEntity mazeEntity = new MazeEntity(base64digest, description);

        MazeDTO mazeDTO = MazeMapper.mapToDTO(mazeEntity, MazeFormat.B64_DIGEST);
        assertEquals(mazeEntity.getId(), mazeDTO.id());
        assertEquals(mazeEntity.getDescription(), mazeDTO.description());
        assertEquals(base64digest, mazeDTO.representation());
        assertEquals(MazeFormat.B64_DIGEST, mazeDTO.mazeFormat());

    }

    @Test
    void testMapToDTOMapsToHexWhenSpecified(){
        MazeEntity mazeEntity = new MazeEntity(base64digest, description);

        MazeDTO mazeDTO = MazeMapper.mapToDTO(mazeEntity, MazeFormat.HEX_DIGEST);
        assertEquals(mazeEntity.getId(), mazeDTO.id());
        assertEquals(mazeEntity.getDescription(), mazeDTO.description());
        assertEquals(hexDigest.toLowerCase(), mazeDTO.representation().toLowerCase());
        assertEquals(MazeFormat.HEX_DIGEST, mazeDTO.mazeFormat());

    }

    @Test
    void testMapToDTOMapsToASCIIWhenSpecified(){
        MazeEntity mazeEntity = new MazeEntity(base64digest, description);

    MazeDTO mazeDTO = MazeMapper.mapToDTO(mazeEntity, MazeFormat.ASCII_GRID);
        assertEquals(mazeEntity.getId(), mazeDTO.id());
        assertEquals(mazeEntity.getDescription(), mazeDTO.description());
        assertEquals(asciiGrid, mazeDTO.representation());
        assertEquals(MazeFormat.ASCII_GRID, mazeDTO.mazeFormat());

    }


    @Test
    void testMapToEntity(){
        MazeDTO mazeDTO = new MazeDTO(id, description, base64digest, MazeFormat.B64_DIGEST);

        MazeEntity mazeEntity = MazeMapper.mapToEntity(mazeDTO);

        assertEquals(mazeDTO.id(), mazeEntity.getId());
    }

    @Test
    void testMapToEntitySavesRepresentationAsB64DigestWhenDTOWasB64Digest(){
        MazeDTO mazeDTO = new MazeDTO(id, description, base64digest, MazeFormat.B64_DIGEST);

        MazeEntity mazeEntity = MazeMapper.mapToEntity(mazeDTO);

        assertEquals(base64digest, mazeEntity.getRawRepresentation());

    }

    @Test
    void testMapToEntitySavesRepresentationAsB64DigestWhenDTOWasHexDigest(){
        MazeDTO mazeDTO = new MazeDTO(id, description, hexDigest, MazeFormat.HEX_DIGEST);

        MazeEntity mazeEntity = MazeMapper.mapToEntity(mazeDTO);

        assertEquals(base64digest, mazeEntity.getRawRepresentation());

    }



    @Test
    void testMapToEntitySavesRepresentationAsB64DigestWhenDTOWasASCIIGrid(){
            
    }

}
