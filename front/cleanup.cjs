import depcheck from 'depcheck';
import { exec } from 'child_process';

const options = {
  ignoreDirs: [
    'node_modules',
    'dist'
  ],
  ignoreMatches: [
    'eslint', 'eslint-*', // Ajuste conforme necessário
  ],
};

depcheck(__dirname, options, unused => {
  console.log('Unused dependencies');
  console.log(unused.dependencies);

  console.log('Unused devDependencies');
  console.log(unused.devDependencies);

  const allUnusedDeps = unused.dependencies.concat(unused.devDependencies);
  if (allUnusedDeps.length === 0) {
    console.log('No unused dependencies found.');
    return;
  }

  const uninstallCommand = `npm uninstall ${allUnusedDeps.join(' ')}`;
  console.log(`Running: ${uninstallCommand}`);

  exec(uninstallCommand, (err, stdout, stderr) => {
    if (err) {
      console.error(`Error: ${err}`);
      return;
    }

    console.log(`stdout: ${stdout}`);
    console.error(`stderr: ${stderr}`);
  });
});
