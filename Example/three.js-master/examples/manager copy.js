
import * as THREE from 'three';
import { ArcballControls } from 'three/addons/controls/ArcballControls.js';
//import { OBJLoader } from 'three/addons/loaders/OBJLoader.js';
const perspectiveDistance = 2.5;
let camera, controls, scene, renderer;
init();
async function init() {
    const container = document.createElement('div');
    document.body.appendChild(container);
    renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
    renderer.setPixelRatio(window.devicePixelRatio);
    renderer.setSize(window.innerWidth, window.innerHeight);
    renderer.outputEncoding = THREE.sRGBEncoding;
    renderer.toneMapping = THREE.ReinhardToneMapping;
    renderer.toneMappingExposure = 3;
    renderer.domElement.style.background = 'linear-gradient( 180deg, rgba( 0,0,0,1 ) 0%, rgba( 128,128,255,1 ) 100% )';
    container.appendChild(renderer.domElement);

    scene = new THREE.Scene();
    scene.add(new THREE.AmbientLight(0xffffff, 0.6));
    const axesHelper = new THREE.AxesHelper(290);
    scene.add(axesHelper);
    camera = makePerspectiveCamera();
    camera.position.set(290, 290, 290);
    occtimportjs().then(async function (occt) {
        let fileUrl = 'models/obj/cerberus/220613_punto_di_presa_robot.stp';
        let response = await fetch(fileUrl);
        let buffer = await response.arrayBuffer();
        let fileBuffer = new Uint8Array(buffer);
        let result = occt.ReadStepFile(fileBuffer);
        console.log(result);
        for (let resultMesh of result.meshes) {
            let geometry = new THREE.BufferGeometry();
            geometry.setAttribute('position', new THREE.Float32BufferAttribute(resultMesh.attributes.position.array, 3));
            if (resultMesh.attributes.normal) {
                geometry.setAttribute('normal', new THREE.Float32BufferAttribute(resultMesh.attributes.normal.array, 3));
            }
            const index = Uint16Array.from(resultMesh.index.array);
            geometry.setIndex(new THREE.BufferAttribute(index, 1));
            // let material = null;
            // if (resultMesh.color) {
            //     const color = new THREE.Color(resultMesh.color[0], resultMesh.color[1], resultMesh.color[2]);
            //     material = new THREE.MeshPhongMaterial({ color: color });
            // } else {
            //     material = new THREE.MeshPhongMaterial({ color: 0xcccccc });
            // }
            // mesh
            var material = new THREE.LineBasicMaterial({
                color: 0xff0000,
                polygonOffset: true,
                polygonOffsetFactor: 1, // positive value pushes polygon further away
                polygonOffsetUnits: 1
            });
            var mesh = new THREE.Mesh(geometry, material);
            scene.add(mesh)

            // // wireframe
            // var geo = new THREE.EdgesGeometry(mesh.geometry); // or WireframeGeometry
            // var mat = new THREE.LineBasicMaterial({ color: 0xffffff });
            // var wireframe = new THREE.LineSegments(geo, mat);
            // mesh.add(wireframe);

            let eg = EdgesGeometry(geometry);
            let m = new THREE.ShaderMaterial({
                vertexShader: conditionalLineVertShader,
                fragmentShader: conditionalLineFragShader,
                uniforms: {
                    diffuse: {
                        value: new THREE.Color(color)
                    },
                    opacity: {
                        value: 0
                    }
                },
                transparent: false
            });
            let o = new THREE.LineSegments(eg, m);
            mesh.add(o);

            render();
        }
    });
    render();
    window.addEventListener('resize', onWindowResize);
    setArcballControls();
    render();

}

function setArcballControls() {
    controls = new ArcballControls(camera, renderer.domElement, scene);
    controls.addEventListener('change', render);
    controls.setGizmosVisible(false);
}

function makePerspectiveCamera() {
    const fov = 75;
    const aspect = window.innerWidth / window.innerHeight;
    const near = 1;
    const far = 2000;
    const newCamera = new THREE.PerspectiveCamera(fov, aspect, near, far);
    return newCamera;
}

function onWindowResize() {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
    render();
}

function render() {
    renderer.render(scene, camera);
}

function EdgesGeometry(geometry, thresholdAngle) {

    let g = new THREE.InstancedBufferGeometry();

    g.type = 'EdgesGeometry';

    g.parameters = {
        thresholdAngle: thresholdAngle
    };

    thresholdAngle = (thresholdAngle !== undefined) ? thresholdAngle : 1;

    // buffer

    const vertices = [];
    const control0 = [];
    const control1 = [];
    const direction = [];
    const collapse = [];

    // helper variables

    const thresholdDot = Math.cos(THREE.MathUtils.DEG2RAD * thresholdAngle);
    const edge = [0, 0], edges = {};
    let edge1, edge2, key;
    const keys = ['a', 'b', 'c'];

    // prepare source geometry

    let geometry2;

    if (geometry.isBufferGeometry) {

        geometry2 = new THREE.Geometry();
        geometry2.fromBufferGeometry(geometry);

    } else {

        geometry2 = geometry.clone();

    }

    geometry2.mergeVertices();
    geometry2.computeFaceNormals();

    const sourceVertices = geometry2.vertices;
    const faces = geometry2.faces;

    // now create a data structure where each entry represents an edge with its adjoining faces

    for (let i = 0, l = faces.length; i < l; i++) {

        const face = faces[i];

        for (let j = 0; j < 3; j++) {

            edge1 = face[keys[j]];
            edge2 = face[keys[(j + 1) % 3]];
            edge[0] = Math.min(edge1, edge2);
            edge[1] = Math.max(edge1, edge2);

            key = edge[0] + ',' + edge[1];

            if (edges[key] === undefined) {

                edges[key] = { index1: edge[0], index2: edge[1], face1: i, face2: undefined };

            } else {

                edges[key].face2 = i;

            }

        }

    }

    // generate vertices
    const v3 = new THREE.Vector3();
    const n = new THREE.Vector3();
    const n1 = new THREE.Vector3();
    const n2 = new THREE.Vector3();
    const d = new THREE.Vector3();
    for (key in edges) {

        const e = edges[key];

        // an edge is only rendered if the angle (in degrees) between the face normals of the adjoining faces exceeds this value. default = 1 degree.

        if (e.face2 === undefined || faces[e.face1].normal.dot(faces[e.face2].normal) <= thresholdDot) {

            let vertex1 = sourceVertices[e.index1];
            let vertex2 = sourceVertices[e.index2];

            vertices.push(vertex1.x, vertex1.y, vertex1.z);
            vertices.push(vertex2.x, vertex2.y, vertex2.z);

            d.subVectors(vertex2, vertex1);
            collapse.push(0, 1);
            n.copy(d).normalize();
            direction.push(d.x, d.y, d.z);
            n1.copy(faces[e.face1].normal);
            n1.crossVectors(n, n1);
            d.subVectors(vertex1, vertex2);
            n.copy(d).normalize();
            n2.copy(faces[e.face2].normal);
            n2.crossVectors(n, n2);
            direction.push(d.x, d.y, d.z);

            v3.copy(vertex1).add(n1); // control0
            control0.push(v3.x, v3.y, v3.z);
            v3.copy(vertex1).add(n2); // control1
            control1.push(v3.x, v3.y, v3.z);

            v3.copy(vertex2).add(n1); // control0
            control0.push(v3.x, v3.y, v3.z);
            v3.copy(vertex2).add(n2); // control1
            control1.push(v3.x, v3.y, v3.z);
        }

    }

    // build geometry

    g.setAttribute('position', new THREE.Float32BufferAttribute(vertices, 3));
    g.setAttribute('control0', new THREE.Float32BufferAttribute(control0, 3));
    g.setAttribute('control1', new THREE.Float32BufferAttribute(control1, 3));
    g.setAttribute('direction', new THREE.Float32BufferAttribute(direction, 3));
    g.setAttribute('collapse', new THREE.Float32BufferAttribute(collapse, 1));
    return g;

}