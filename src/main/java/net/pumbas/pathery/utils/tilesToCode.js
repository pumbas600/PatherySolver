async function encodeMap(mapId) {
	try {
		const res = await fetch(`https://www.pathery.com/a/map/${mapId}.js`);
		const json = await res.json();
		encodeTiles(json.tiles);
	} catch (error) {
		console.error('Error fetching map:', error);
	}
}

function encodeTiles(tiles) {
	let codedMap = '';

	for (const row of tiles) {
		codedMap += '"';
		for (const column of row) {
			const [tile] = column;
			codedMap += tile;
		}
		codedMap += '",\n';
	}

	console.log(codedMap);
}
