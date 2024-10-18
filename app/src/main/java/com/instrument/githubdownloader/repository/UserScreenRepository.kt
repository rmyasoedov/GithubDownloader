package com.instrument.githubdownloader.repository

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.instrument.githubdownloader.R
import com.instrument.githubdownloader.ui.DownloadFragment
import com.instrument.githubdownloader.ui.MainActivity
import com.instrument.githubdownloader.ui.RepositoryFragment
import com.instrument.githubdownloader.ui.SearchFragment
import com.instrument.githubdownloader.util.KeyString
import javax.inject.Inject

class UserScreenRepository @Inject constructor(private val appCompatActivity: AppCompatActivity) {

    fun openSearchFragment(){
        val fragment = SearchFragment()
        openFragment(fragment)
    }

    fun openDownloadFragment(){
        val fragment = DownloadFragment()
        openFragment(fragment)
    }

    fun openRepositoryFragment(user: String, repository: String){
        val fragment = RepositoryFragment()
        fragment.arguments = Bundle().apply {
            putString(KeyString.USER, user)
            putString(KeyString.REPOSITORY, repository)
        }
        openFragment(fragment)
    }

    fun openBranchLink(link: String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        appCompatActivity.startActivity(intent)
    }

    private fun openFragment(
        fragment: Fragment,
        pDeletePreviewFragmentInBackstack: Boolean = false
    ) {
        val fragmentManager = appCompatActivity.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()


        fragmentTransaction.replace(R.id.fContainerView, fragment,fragment::class.simpleName)
        fragmentTransaction.addToBackStack("${fragment::class.simpleName}")

        if(pDeletePreviewFragmentInBackstack){
            fragmentManager.popBackStack()
        }

        fragmentTransaction.commit()
    }
}