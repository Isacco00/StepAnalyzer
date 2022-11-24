import math
import stl
from stl import mesh
import numpy
import sys

# find the max dimensions, so we can know the bounding box, getting the height,
# width, length (because these are the step size)...
def findMinsMaxs(obj):
    minx = obj.x.min()
    maxx = obj.x.max()
    miny = obj.y.min()
    maxy = obj.y.max()
    minz = obj.z.min()
    maxz = obj.z.max()
    return minx, maxx, miny, maxy, minz, maxz

def main():
    if(len(sys.argv) < 2):
        print("No file passed")
        sys.exit(2)
    mainObject = mesh.Mesh.from_file(sys.argv[1])
    minx, maxx, miny, maxy, minz, maxz = findMinsMaxs(mainObject)
    w = maxx - minx
    l = maxy - miny
    h = maxz - minz
    print(str(w) + ";" + str(l) + ";" + str(h))
    volume, cog, inertia = mainObject.get_mass_properties()
    print("{0}".format(volume))
    
    
if __name__ == "__main__":
  main()